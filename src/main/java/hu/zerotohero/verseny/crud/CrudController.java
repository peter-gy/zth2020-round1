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
        try {
            return new ResponseEntity<>(locationService.createLocation(location), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/equipment")
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment) {
        try {
            return new ResponseEntity<>(equipmentService.createEquipment(equipment), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/employee")
    public ResponseEntity<Employee> createEmployee(@RequestBody Employee employee) {
        try {
            return new ResponseEntity<>(employeeService.createEmployee(employee), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/location/{id}")
    public ResponseEntity<Location> updateLocation(@PathVariable("id") Long id, @RequestBody Location location) {
        try {
            return new ResponseEntity<>(locationService.updateLocation(id, location), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/equipment/{id}")
    public ResponseEntity<Equipment> updateEquipment(@PathVariable("id") Long id, @RequestBody Equipment equipment) {
        try {
            return new ResponseEntity<>(equipmentService.updateEquipment(id, equipment), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/employee/{id}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable("id") Long id, @RequestBody Employee employee) {
        try {
            return new ResponseEntity<>(employeeService.updateEmployee(id, employee), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/location/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable("id") Long id) {
        try {
            locationService.deleteLocation(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/equipment/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable("id") Long id) {
        try {
            equipmentService.deleteEquipment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/employee/{id}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable("id") Long id) {
        try {
            equipmentService.deleteEquipment(id);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

}
