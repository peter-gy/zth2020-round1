package hu.zerotohero.verseny.crud.repository;

import hu.zerotohero.verseny.crud.entity.Employee;
import org.springframework.data.repository.CrudRepository;

public interface EmployeeRepository extends CrudRepository<Employee, Long> {}
