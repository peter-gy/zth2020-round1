package hu.zerotohero.verseny.crud.service;

import hu.zerotohero.verseny.crud.entity.Employee;
import hu.zerotohero.verseny.crud.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    @Autowired
    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee createEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee updateEmployee(Long id, Employee employee) {
        Employee toUpdate = employeeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        toUpdate.setName(employee.getName());
        toUpdate.setJob(employee.getJob());
        toUpdate.setWorksat(employee.getWorksat());
        toUpdate.setOperates(employee.getOperates());
        return employeeRepository.save(toUpdate);
    }

    @Override
    public void deleteEmployee(Long id) {
        Employee toDelete = employeeRepository.findById(id).orElseThrow(IllegalArgumentException::new);
        employeeRepository.delete(toDelete);
    }
}
