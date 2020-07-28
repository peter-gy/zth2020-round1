package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.exception.*;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import hu.zerotohero.verseny.crud.util.EntityValidator;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Comparator;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {

    private final LocationRepository locationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee createEmployee(Employee employee) {
        validateDependencyExistence(employee);
        employee.setWorksAt(locationRepository.findById(employee.getWorksAt().getId()).get());
        employee.setOperates(equipmentRepository.findById(employee.getOperates().getId()).get());

        validateInput(employee);
        validatePlacementLogic(employee);
        validateEmploymentLogic(employee);
        validateEquipmentAssignment(employee);
        validateSalary(employee);

        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee toUpdate = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No employee with an id of " + id + " was found"));

        PropertyCopier.copyNonNullProperties(employee, toUpdate);

        validateDependencyExistence(toUpdate);
        toUpdate.setWorksAt(locationRepository.findById(toUpdate.getWorksAt().getId()).get());
        toUpdate.setOperates(equipmentRepository.findById(toUpdate.getOperates().getId()).get());

        validateInput(toUpdate);
        validatePlacementLogic(toUpdate);
        validateJobUpdateLogic(toUpdate);
        validateEquipmentAssignment(toUpdate);
        validateSalary(toUpdate);

        return employeeRepository.save(toUpdate);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee toDelete = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No employee with an id of " + id + " was found"));
        employeeRepository.delete(toDelete);
    }

    // Pre-creation and Pre-update
    private void validateDependencyExistence(Employee employee) {
        Long worksAtId = employee.getWorksAt().getId();
        if (!locationRepository.findById(worksAtId).isPresent())
            throw new EntityDependenceException("Location of employee is not defined yet");

        Long operatesId = employee.getOperates().getId();
        if (!equipmentRepository.findById(operatesId).isPresent())
            throw new EntityDependenceException("Equipment of employee is not defined yet");
    }

    // Pre-creation and Pre-update
    private void validatePlacementLogic(Employee employee) {
        Long workLocationId = employee.getWorksAt().getId();
        Long operatedEquipmentLocationId = employee.getOperates().getLocatedAt().getId();
        if (!workLocationId.equals(operatedEquipmentLocationId))
            throw new IllegalPlacementException("The operated equipment and the employee must be at the same location");
    }

    // Pre-creation
    private void validateEmploymentLogic(Employee employee) {
        switch (employee.getJob()) {
            case MANAGER:
                if (!isManagerEmployable(employee))
                    throw new TooManyManagersException("Only one manager can work at a location");
                break;
            case CASHIER:
                if (!isCashierEmployable(employee))
                    throw new InsufficientEquipmentException("There is not enough cash register at the location");
                break;
            case COOK:
                if (!isCookEmployable(employee))
                    throw new InsufficientEquipmentException("There is not enough oven at the location");
                break;
        }
    }

    // Pre-update
    private void validateJobUpdateLogic(Employee employee) {
        switch (employee.getJob()) {
            case MANAGER:
                if (!isUpdatableToManager(employee))
                    throw new TooManyManagersException("Only one manager can work at a location");
                break;
            case CASHIER:
                if (!isUpdatableToCashier(employee))
                    throw new InsufficientEquipmentException("There is not enough cash register at the location");
                break;
            case COOK:
                if (!isUpdatableToCook(employee))
                    throw new InsufficientEquipmentException("There is not enough oven at the location");
                break;
        }
    }

    // Pre-create and Pre-update
    private void validateEquipmentAssignment(Employee employee) {
        if (employee.getJob() == Employee.Job.COOK && employee.getOperates().getType() == Equipment.Type.CASH_REGISTER)
            throw new IllegalEquipmentAssignmentException("A Cook cannot operate a cash register");
        if (employee.getJob() == Employee.Job.CASHIER && employee.getOperates().getType() == Equipment.Type.OVEN)
            throw new IllegalEquipmentAssignmentException("A cashier cannot operate an oven");
    }

    private void validateSalary(Employee employee) {
        // check global salary
        if (employee.getSalary() < 300)
            throw new HumiliatingSalaryException("The minimal salary is 300 kopejka. Don't be heartless.");

        // check local minimal salary
        int minimalSalaryAtLocationForJob = getMinimalSalaryAtLocationForJob(employee.getWorksAt(), employee.getJob());
        if (employee.getSalary() < minimalSalaryAtLocationForJob) {
            String message = String.format("The salary [%d] cannot be smaller than the local minimal salary [%d].",
                    employee.getSalary(),
                    minimalSalaryAtLocationForJob);
            throw new HumiliatingSalaryException(message);
        }

        // check manager salary
        int maximalSalaryAtLocation = getMaximalSalaryAtLocation(employee.getWorksAt());
        if (employee.getJob() == Employee.Job.MANAGER && employee.getSalary() < maximalSalaryAtLocation) {
            String message = String.format("The manager's salary [%d] must be greater than the local maximum salary [%d].",
                    employee.getSalary(),
                    maximalSalaryAtLocation);
            throw new HumiliatingSalaryException(message);
        }

        // check non-manager salary
        double averageSalaryAtLocationForJob = getAverageSalaryAtLocationForJob(employee.getWorksAt(), employee.getJob());
        if (averageSalaryAtLocationForJob == -1) return;
        final double factor = 0.2;
        final double maxDifference = factor * averageSalaryAtLocationForJob;
        if (employee.getJob() != Employee.Job.MANAGER &&
            Math.abs(averageSalaryAtLocationForJob - employee.getSalary()) > maxDifference) {
            String message = String.format("The worker's salary [%d] must not differ by more than [%d]%% from the average salary [%d].",
                    employee.getSalary(),
                    (int)(factor * 100),
                    maximalSalaryAtLocation);
            throw new HumiliatingSalaryException(message);
        }
    }

    private void validateInput(Employee employee) {
        EntityValidator.validateEmployee(employee);
    }

    private boolean isManagerEmployable(Employee employee) {
        long managerCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.MANAGER);
        return managerCountAtLocation == 0;
    }

    private boolean isUpdatableToManager(Employee employee) {
        long managerCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.MANAGER);
        return managerCountAtLocation == 1;
    }

    private boolean isCashierEmployable(Employee employee) {
        long cashRegisterCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.CASH_REGISTER);
        long cashierCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.CASHIER);
        return cashRegisterCountAtLocation > cashierCountAtLocation;
    }

    private boolean isUpdatableToCashier(Employee employee) {
        long cashRegisterCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.CASH_REGISTER);
        long cashierCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.CASHIER);
        return cashRegisterCountAtLocation >= cashierCountAtLocation;
    }

    private boolean isCookEmployable(Employee employee) {
        long ovenCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.OVEN);
        long cookCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.COOK);
        return ovenCountAtLocation > cookCountAtLocation;
    }

    private boolean isUpdatableToCook(Employee employee) {
        long ovenCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.OVEN);
        long cookCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.COOK);
        return ovenCountAtLocation >= cookCountAtLocation;
    }

    private long employeeCountAtLocation(Location location, Employee.Job job) {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(e -> e.getWorksAt().getId().equals(location.getId()) && e.getJob() == job)
                .count();
    }

    private long equipmentCountAtLocation(Location location, Equipment.Type type) {
        return StreamSupport.stream(equipmentRepository.findAll().spliterator(), false)
                .filter(equipment -> equipment.getLocatedAt().getId().equals(location.getId()) && equipment.getType() == type)
                .count();
    }

    private int getMinimalSalaryAtLocationForJob(Location location, Employee.Job job) {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(e -> e.getWorksAt().getId().equals(location.getId()) && e.getJob() == job)
                .min(Comparator.comparingInt(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(-1);
    }

    private int getMaximalSalaryAtLocation(Location location) {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(e -> e.getWorksAt().getId().equals(location.getId()))
                .max(Comparator.comparingInt(Employee::getSalary))
                .map(Employee::getSalary)
                .orElse(-1);
    }

    private double getAverageSalaryAtLocationForJob(Location location, Employee.Job job) {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(e -> e.getWorksAt().getId().equals(location.getId()) && e.getJob() == job)
                .mapToInt(Employee::getSalary)
                .average()
                .orElse(-1.0);
    }
}
