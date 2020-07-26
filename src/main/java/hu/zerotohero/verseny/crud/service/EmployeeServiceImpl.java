package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.exception.EntityDependenceException;
import hu.zerotohero.verseny.crud.exception.IllegalPlacementException;
import hu.zerotohero.verseny.crud.exception.InsufficientEquipmentException;
import hu.zerotohero.verseny.crud.exception.TooManyManagersException;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.stream.StreamSupport;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final LocationRepository locationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(LocationRepository locationRepository,
                               EquipmentRepository equipmentRepository,
                               EmployeeRepository employeeRepository) {
        this.locationRepository = locationRepository;
        this.equipmentRepository = equipmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee toUpdate = employeeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No employee with an id of " + id + " was found"));
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
        if (employee.getWorksAt() == null) return;
        Long worksAtId = employee.getWorksAt().getId();
        if (!locationRepository.findById(worksAtId).isPresent())
            throw new EntityDependenceException("Location of employee is not defined yet");

        if (employee.getOperates() == null) return;
        Long operatesId = employee.getOperates().getId();
        if (!equipmentRepository.findById(operatesId).isPresent())
            throw new EntityDependenceException("Equipment of employee is not defined yet");
    }

    private void validatePlacementLogic(Employee employee) {
        if (employee.getWorksAt() == null || employee.getOperates().getLocatedAt() == null) return;
        Long worksAtId = employee.getWorksAt().getId();
        Long operatedEquipmentLocationId = employee.getOperates().getLocatedAt().getId();
        if (!worksAtId.equals(operatedEquipmentLocationId))
            throw new IllegalPlacementException("The operated equipment and the employee must be at the same location");
    }

    private void validateEmploymentLogic(Employee employee) {
        if (employee.getJob() == null) return;
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

    private boolean isManagerEmployable(Employee employee) {
        long managerCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.MANAGER);
        return managerCountAtLocation == 0;
    }

    private boolean isCashierEmployable(Employee employee) {
        long cashRegisterCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.CASH_REGISTER);
        long cashierCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.CASHIER);
        return cashRegisterCountAtLocation > cashierCountAtLocation;
    }

    private boolean isCookEmployable(Employee employee) {
        long ovenCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.OVEN);
        long cookCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.COOK);
        return ovenCountAtLocation > cookCountAtLocation;
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
}
