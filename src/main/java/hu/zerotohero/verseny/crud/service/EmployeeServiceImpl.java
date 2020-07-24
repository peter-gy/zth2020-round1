package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
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
        switch (employee.getJob()) {
            case MANAGER:
                if (!isManagerEmployable(employee))
                    throw new IllegalArgumentException("Only one manager can work at a location");
                break;
            case CASHIER:
                if (!isCashierEmployable(employee))
                    throw new IllegalArgumentException("There is not enough cash registers at the location");
                break;
            case COOK:
                if (!isCookEmployable(employee))
                    throw new IllegalArgumentException("There is not enough ovens at the location");
                break;
        }
        return employeeRepository.save(employee);
    }

    private boolean isManagerEmployable(Employee employee) {
        if (employee.getJob() != Employee.Job.MANAGER)
            throw new IllegalArgumentException("Employee is not a manager!");
        long managerCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.MANAGER);
        return managerCountAtLocation == 0;
    }

    private boolean isCashierEmployable(Employee employee) {
        if (employee.getJob() != Employee.Job.CASHIER)
            throw new IllegalArgumentException("Employee is not a cashier!");
        long cashRegisterCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.CASH_REGISTER);
        long cashierCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.CASHIER);
        return cashRegisterCountAtLocation > cashierCountAtLocation;
    }

    private boolean isCookEmployable(Employee employee) {
        if (employee.getJob() != Employee.Job.COOK)
            throw new IllegalArgumentException("Employee is not a cook!");
        long ovenCountAtLocation = equipmentCountAtLocation(employee.getWorksAt(), Equipment.Type.OVEN);
        long cookCountAtLocation = employeeCountAtLocation(employee.getWorksAt(), Employee.Job.COOK);
        return ovenCountAtLocation > cookCountAtLocation;
    }

    private long employeeCountAtLocation(Location location, Employee.Job job) {
        return StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(e -> e.getWorksAt().equals(location) && e.getJob() == job)
                .count();
    }

    private long equipmentCountAtLocation(Location location, Equipment.Type type) {
        return StreamSupport.stream(equipmentRepository.findAll().spliterator(), false)
                .filter(equipment -> equipment.getLocatedAt().equals(location) && equipment.getType() == type)
                .count();
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee toUpdate = employeeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        PropertyCopier.copyNonNullProperties(employee, toUpdate);
        return employeeRepository.save(toUpdate);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee toDelete = employeeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        employeeRepository.delete(toDelete);
    }
}
