package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    Optional<Employee> findByMaNV(String maNV);
    Optional<Employee> findByUsername(String username);
}
