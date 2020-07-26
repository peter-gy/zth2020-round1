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
@RequestMapping(path = "/api", headers = {"Accept=application/json", "Content-type=application/json"})
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
        Location loc = locationService.createLocation(location);
        return new ResponseEntity<>(loc, HttpStatus.CREATED);
    }

    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        Equipment eq = equipmentService.createEquipment(equipment);
        return new ResponseEntity<>(eq, HttpStatus.CREATED);
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        Employee emp = employeeService.createEmployee(employee);
        return new ResponseEntity<>(emp, HttpStatus.CREATED);
    }

    @PutMapping("/location/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") Long id, @RequestBody Location location) {
        return new ResponseEntity<>(locationService.updateLocation(id, location), HttpStatus.OK);
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long id, @RequestBody Equipment equipment) {
        return new ResponseEntity<>(equipmentService.updateEquipment(id, equipment), HttpStatus.OK);
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        equipmentService.deleteEquipment(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleException(Exception ex) {
        String message = String.format("%s: %s", ex.getClass().getSimpleName(), ex.getLocalizedMessage());
        return new ResponseEntity<>(message, HttpStatus.BAD_REQUEST);
    }

}
