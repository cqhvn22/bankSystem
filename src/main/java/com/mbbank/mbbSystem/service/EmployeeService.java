package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.model.Branch;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import com.mbbank.mbbSystem.repository.BranchRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private BranchRepository branchRepository;

    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedInfo) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        employee.setFullName(updatedInfo.getFullName());
        employee.setPosition(updatedInfo.getPosition());
        if (updatedInfo.getRole() != null) employee.setRole(updatedInfo.getRole());
        if (updatedInfo.getMaNV() != null) employee.setMaNV(updatedInfo.getMaNV());
        if (updatedInfo.getBoPhan() != null) employee.setBoPhan(updatedInfo.getBoPhan());
        if (updatedInfo.getLuong() != null) employee.setLuong(updatedInfo.getLuong());
        if (updatedInfo.getEmail() != null) employee.setEmail(updatedInfo.getEmail());
        return employeeRepository.save(employee);
    }

    public void lockEmployeeAccount(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        employee.setStatus("LOCKED");
        employeeRepository.save(employee);
    }

    public void unlockEmployeeAccount(Long id) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        employee.setStatus("ACTIVE");
        employeeRepository.save(employee);
    }

    public Employee changeRole(Long id, String newRole) {
        Employee employee = employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        employee.setRole(newRole);
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        employeeRepository.deleteById(id);
    }

    public Optional<Employee> getEmployee(Long id) {
        return employeeRepository.findById(id);
    }

    public Optional<Employee> getEmployeeByMaNV(String maNV) {
        return employeeRepository.findByMaNV(maNV);
    }

    public void assignBranch(Long employeeId, Long branchId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy nhân viên"));
        Branch branch = branchRepository.findById(branchId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy chi nhánh"));
        employee.setBranch(branch);
        employeeRepository.save(employee);
    }

    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public List<Employee> getEmployeesByBranch(Long branchId) {
        return employeeRepository.findByBranchId(branchId);
    }

    /**
     * Kiểm tra chi nhánh đã có quản lý chưa (dùng khi thêm nhân viên ROLE_BRANCH_MANAGER)
     */
    public boolean branchAlreadyHasManager(Long branchId) {
        return employeeRepository.existsByBranchIdAndRole(branchId, "ROLE_BRANCH_MANAGER");
    }
}
