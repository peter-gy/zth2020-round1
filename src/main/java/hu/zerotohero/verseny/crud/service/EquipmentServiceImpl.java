package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.exception.EntityDependenceException;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import hu.zerotohero.verseny.crud.repository.EquipmentRepository;
import hu.zerotohero.verseny.crud.repository.LocationRepository;
import hu.zerotohero.verseny.crud.util.EntityValidator;
import hu.zerotohero.verseny.crud.util.PropertyCopier;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class EquipmentServiceImpl implements EquipmentService {

    private final LocationRepository locationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Equipment createEquipment(Equipment equipment) {
        validateDependencyExistence(equipment);
        validateInput(equipment);
        equipment.setLocatedAt(locationRepository.findById(equipment.getLocatedAt().getId()).get());
        return equipmentRepository.save(equipment);
    }

    @Override
    public Equipment updateEquipment(Long id, Equipment equipment) {
        Equipment toUpdate = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No equipment with an id of " + id + " was found"));
        PropertyCopier.copyNonNullProperties(equipment, toUpdate);

        validateDependencyExistence(toUpdate);
        validateInput(toUpdate);
        toUpdate.setLocatedAt(locationRepository.findById(toUpdate.getLocatedAt().getId()).get());
        validatePlacementLogic(id);

        return equipmentRepository.save(toUpdate);
    }

    @Override
    public void deleteEquipment(Long id) {
        Equipment toDelete = equipmentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No equipment with an id of " + id + " was found"));
        validateEntityDependence(id);
        equipmentRepository.delete(toDelete);
    }

    // Pre-creation and Pre-update
    private void validateDependencyExistence(Equipment equipment) {
        Long locatedAtId = equipment.getLocatedAt().getId();
        if (!locationRepository.findById(locatedAtId).isPresent())
            throw new EntityDependenceException("Location of equipment is not defined yet");
    }

    // Pre-update
    private void validatePlacementLogic(Long id) {
        List<Employee> dependentEmployees = StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(employee -> employee.getOperates().getId().equals(id))
                .collect(Collectors.toList());

        boolean conflict = dependentEmployees.stream()
                .anyMatch(employee -> !employee.getOperates().getLocatedAt().getId().equals(employee.getWorksAt().getId()));

        if (!dependentEmployees.isEmpty() && conflict) {
            String dependentEmployeeIds = dependentEmployees.stream()
                    .map(Employee::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            String message = String.format("Some entities depend on this equipment. " +
                            "Dependent Employee IDs: [%s].",
                            dependentEmployeeIds);
            throw new EntityDependenceException(message);
        }
    }

    private void validateInput(Equipment equipment) {
        EntityValidator.validateEquipment(equipment);
    }

    // Pre-deletion
    private void validateEntityDependence(Long id) {
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
    }

}
