package org.abol.retentiontrak.repository;


import org.abol.retentiontrak.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
