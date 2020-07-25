package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.exception.EntityDependenceException;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final LocationRepository locationRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public EquipmentServiceImpl(EquipmentRepository equipmentRepository,
                                LocationRepository locationRepository,
                                EmployeeRepository employeeRepository) {
        this.equipmentRepository = equipmentRepository;
        this.locationRepository = locationRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Equipment createEquipment(Equipment equipment) {
        // validate dependency
        Long locatedAtId = equipment.getLocatedAt().getId();
        if (!locationRepository.findById(locatedAtId).isPresent())
            throw new EntityDependenceException("Location of equipment is not defined yet");

        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(Long id, Equipment equipment) {
        Equipment toUpdate = equipmentRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        PropertyCopier.copyNonNullProperties(equipment, toUpdate);
        return equipmentRepository.save(toUpdate);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment toDelete = equipmentRepository.findById(id).orElseThrow(IllegalArgumentException::new);

        // check dependencies before deletion
        List<Employee> dependentEmployees = StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(employee -> employee.getOperates().getId().equals(id))
                .collect(Collectors.toList());

        if (!dependentEmployees.isEmpty()) {
            String dependentEmployeeIds = dependentEmployees.stream()
                    .map(Employee::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            String message = String.format("Some entities depend on this equipment. " +
                            "Dependent Employee IDs: [%s].",
                            dependentEmployeeIds);
            throw new EntityDependenceException(message);
        }

        equipmentRepository.delete(toDelete);
    }
}
