package hu.zerotohero.verseny;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.entity.Equipment;
import hu.zerotohero.verseny.crud.entity.Location;
import hu.zerotohero.verseny.crud.exception.HumiliatingSalaryException;
import hu.zerotohero.verseny.crud.exception.IllegalPlacementException;
import hu.zerotohero.verseny.crud.exception.InsufficientEquipmentException;
import hu.zerotohero.verseny.crud.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
class VersenyApplicationTests {

    private final LocationService locationService;
    private final EquipmentService equipmentService;
    private final EmployeeService employeeService;

    private List<Location> locations;
    private List<Equipment> equipments;

    @Autowired
    public VersenyApplicationTests(LocationService locationService, EquipmentService equipmentService, EmployeeService employeeService) {
        this.locationService = locationService;
        this.equipmentService = equipmentService;
        this.employeeService = employeeService;
    }

    @BeforeEach
    void init() {
        locations = createLocations();
        equipments = createEquipments(locations);
        locations.forEach(locationService::createLocation);
        equipments.forEach(equipmentService::createEquipment);
    }

    @Test
    void test_employee_and_equipment_count() {
        Employee cook0 = getValidCook("Gordon Ramsey", 400);
        Employee cook1 = getValidCook("Massimo Bottura", 440);
        assertDoesNotThrow(() -> employeeService.createEmployee(cook0));
        assertThrows(InsufficientEquipmentException.class,
                () -> employeeService.createEmployee(cook1));

        Employee cashier0 = getValidCashier("Justin Bieber", 300);
        Employee cashier1 = getValidCashier("Dua Lipa", 330);
        assertDoesNotThrow(() -> employeeService.createEmployee(cashier0));
        assertThrows(InsufficientEquipmentException.class,
                () -> employeeService.createEmployee(cashier1));
    }

    @Test
    void test_employee_and_equipment_location() {
        Employee cashier0 = getValidCashier("Justin Bieber", 300);
        assertDoesNotThrow(() -> employeeService.createEmployee(cashier0));
        assertThrows(IllegalPlacementException.class,
                () -> employeeService.createEmployee(getEmployeeWithIllegalPlacement()));
    }

    @Test
    void test_employee_minimal_salary() {
        Employee cashier0 = getValidCashier("Justin Bieber", 300);
        assertDoesNotThrow(() -> employeeService.createEmployee(cashier0));
        assertThrows(HumiliatingSalaryException.class,
                () -> employeeService.createEmployee(getEmployeeWithIllegalSalary()));
    }

    @Test
    void test_manager_salary() {
        Employee cook0 = getValidCook("Gordon Ramsey", 400);
        employeeService.createEmployee(cook0);
        assertThrows(HumiliatingSalaryException.class,
                () -> employeeService.createEmployee(getManager("Michael Scott", 350)));
        assertDoesNotThrow(() -> employeeService.createEmployee(getManager("Pam Beasly", 450)));
    }

    @Test
    void test_average_salary() {
        Employee cook0 = getValidCook("Gordon Ramsey", 400);
        Employee cook1 = getValidCook("Massimo Bottura", 600);

        Equipment oven = new Equipment();
        oven.setId(3L);
        oven.setName("Some Oven");
        oven.setType(Equipment.Type.OVEN);
        oven.setLocatedAt(locations.get(0));
        equipmentService.createEquipment(oven);

        assertDoesNotThrow(() -> employeeService.createEmployee(cook0));
        assertThrows(HumiliatingSalaryException.class,
                () -> employeeService.createEmployee(cook1));
    }

    private Employee getManager(String name, int salary) {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName(name);
        employee.setJob(Employee.Job.MANAGER);
        employee.setSalary(salary);
        employee.setWorksAt(locations.get(0));
        employee.setOperates(equipments.get(0));
        return employee;
    }

    private Employee getValidCook(String name, int salary) {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName(name);
        employee.setJob(Employee.Job.COOK);
        employee.setSalary(salary);
        employee.setWorksAt(locations.get(0));
        employee.setOperates(equipments.get(0));
        return employee;
    }

    private Employee getValidCashier(String name, int salary) {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName(name);
        employee.setJob(Employee.Job.CASHIER);
        employee.setSalary(salary);
        employee.setWorksAt(locations.get(0));
        employee.setOperates(equipments.get(3));
        return employee;
    }

    private Employee getEmployeeWithIllegalSalary() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setJob(Employee.Job.COOK);
        employee.setSalary(200);
        employee.setWorksAt(locations.get(0));
        employee.setOperates(equipments.get(0));
        return employee;
    }

    private Employee getEmployeeWithIllegalPlacement() {
        Employee employee = new Employee();
        employee.setId(1L);
        employee.setName("John Doe");
        employee.setJob(Employee.Job.COOK);
        employee.setSalary(400);
        employee.setWorksAt(locations.get(0));
        employee.setOperates(equipments.get(1));
        return employee;
    }

    private List<Location> createLocations() {
        Location location0 = new Location();
        location0.setId(1L);
        location0.setName("Babel Budapest");
        location0.setAddress("1052 Budapest Piarista köz 2.");

        Location location1 = new Location();
        location1.setId(2L);
        location1.setName("Laurel Budapest");
        location1.setAddress("1073 Budapest Kertész u. 29.");

        return Arrays.asList(location0, location1);
    }

    private List<Equipment> createEquipments(List<Location> locations) {
        Equipment equipment0_loc0 = new Equipment();
        equipment0_loc0.setId(1L);
        equipment0_loc0.setName("Miele H 7890 Bp");
        equipment0_loc0.setType(Equipment.Type.OVEN);
        equipment0_loc0.setLocatedAt(locations.get(0));

        Equipment equipment1_loc1 = new Equipment();
        equipment1_loc1.setId(2L);
        equipment1_loc1.setName("Electrolux X 2301 DL");
        equipment1_loc1.setType(Equipment.Type.CASH_REGISTER);
        equipment1_loc1.setLocatedAt(locations.get(1));

        Equipment equipment2_loc1 = new Equipment();
        equipment2_loc1.setId(3L);
        equipment2_loc1.setName("Miele H 3200 Bp");
        equipment2_loc1.setType(Equipment.Type.OVEN);
        equipment2_loc1.setLocatedAt(locations.get(1));

        Equipment equipment3_loc0 = new Equipment();
        equipment3_loc0.setId(4L);
        equipment3_loc0.setName("Electrolux X 1500 DL");
        equipment3_loc0.setType(Equipment.Type.CASH_REGISTER);
        equipment3_loc0.setLocatedAt(locations.get(0));

        return Arrays.asList(equipment0_loc0, equipment1_loc1, equipment2_loc1, equipment3_loc0);
    }

}
