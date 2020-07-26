package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
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
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;
    private final EquipmentRepository equipmentRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository,
                               EquipmentRepository equipmentRepository,
                               EmployeeRepository employeeRepository) {
        this.locationRepository = locationRepository;
        this.equipmentRepository = equipmentRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Location createLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        Location toUpdate = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No location with an id of " + id + " was found"));
        PropertyCopier.copyNonNullProperties(location, toUpdate);
        return locationRepository.save(toUpdate);
    }

    @Override
    public void deleteLocation(Long id) {
        Location toDelete = locationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No location with an id of " + id + " was found"));
        validateEntityDependence(id);
        locationRepository.delete(toDelete);
    }

    // Pre-delete
    private void validateEntityDependence(Long id) {
        List<Employee> dependentEmployees = StreamSupport.stream(employeeRepository.findAll().spliterator(), false)
                .filter(employee -> employee.getWorksAt().getId().equals(id))
                .collect(Collectors.toList());

        List<Equipment> dependentEquipment = StreamSupport.stream(equipmentRepository.findAll().spliterator(), false)
                .filter(equipment -> equipment.getLocatedAt().getId().equals(id))
                .collect(Collectors.toList());

        if (!(dependentEmployees.isEmpty() && dependentEquipment.isEmpty())) {
            String dependentEmployeeIds = dependentEmployees.stream()
                    .map(Employee::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            String dependentEquipmentIds = dependentEquipment.stream()
                    .map(Equipment::getId)
                    .map(String::valueOf)
                    .collect(Collectors.joining(", "));

            String message = String.format("Some entities depend on this location. " +
                                            "Dependent Equipment IDs: [%s]. " +
                                            "Dependent Employee IDs: [%s].",
                                            dependentEquipmentIds, dependentEmployeeIds);
            throw new EntityDependenceException(message);
        }
    }

}
