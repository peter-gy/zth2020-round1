package hu.zerotohero.verseny.crud;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.service.EmployeeService;
import hu.zerotohero.verseny.crud.service.EquipmentService;
import hu.zerotohero.verseny.crud.service.LocationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class CrudController {

    private final LocationService locationService;
    private final EquipmentService equipmentService;
    private final EmployeeService employeeService;

    public CrudController(LocationService locationService,
                          EquipmentService equipmentService,
                          EmployeeService employeeService) {
        this.locationService = locationService;
        this.equipmentService = equipmentService;
        this.employeeService = employeeService;
    }

    @PostMapping("/location")
    public ResponseEntity<Location> createLocation(@RequestBody Location location) {
        return null;
    }

    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        return null;
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        return null;
    }

    @PutMapping("/location/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") Long id, @RequestBody Location location) {
        return null;
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long id, @RequestBody Equipment equipment) {
        return null;
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        return null;
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        return null;
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        return null;
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        return null;
    }

}
