package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.EmployeeDto;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;
import com.mbbank.mbbSystem.security.UserDetailsImpl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    /** Thêm nhân viên — dùng EmployeeDto */
    @PostMapping("/add")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> addEmployee(@RequestBody EmployeeDto req) {
        try {
            Employee emp = new Employee();
            emp.setUsername(req.getUsername());
            emp.setFullName(req.getFullName());
            emp.setEmail(req.getEmail());
            emp.setMaNV(req.getMaNV());
            emp.setPosition(req.getPosition());
            emp.setBoPhan(req.getBoPhan());
            emp.setLuong(req.getLuong());
            emp.setRole(req.getRole() != null ? req.getRole() : "ROLE_EMPLOYEE");
            Employee saved = employeeService.addEmployee(emp);
            return ResponseEntity.ok(new EmployeeDto(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Cập nhật thông tin nhân viên */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody EmployeeDto req) {
        try {
            Employee patch = new Employee();
            patch.setFullName(req.getFullName());
            patch.setPosition(req.getPosition());
            patch.setRole(req.getRole());
            patch.setMaNV(req.getMaNV());
            patch.setBoPhan(req.getBoPhan());
            patch.setLuong(req.getLuong());
            patch.setEmail(req.getEmail());
            Employee updated = employeeService.updateEmployee(id, patch);
            return ResponseEntity.ok(new EmployeeDto(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Xóa nhân viên */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        try {
            employeeService.deleteEmployee(id);
            return ResponseEntity.ok("Xóa nhân viên thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Khóa tài khoản nhân viên */
    @PutMapping("/lock/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> lockEmployee(@PathVariable Long id) {
        try {
            employeeService.lockEmployeeAccount(id);
            return ResponseEntity.ok("Đã khóa tài khoản nhân viên");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Mở khóa tài khoản nhân viên */
    @PutMapping("/unlock/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> unlockEmployee(@PathVariable Long id) {
        try {
            employeeService.unlockEmployeeAccount(id);
            return ResponseEntity.ok("Đã mở khóa tài khoản nhân viên");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Phân quyền nhân viên */
    @PutMapping("/role/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> changeRole(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            String newRole = body.get("role");
            if (newRole == null || newRole.isBlank())
                return ResponseEntity.badRequest().body("Thiếu trường 'role'");
            Employee updated = employeeService.changeRole(id, newRole);
            return ResponseEntity.ok(new EmployeeDto(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Tìm theo id */
    @GetMapping("/search/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        return employeeService.getEmployee(id)
                .map(e -> ResponseEntity.ok(new EmployeeDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Tìm theo mã NV */
    @GetMapping("/search")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> searchByMaNV(@RequestParam String maNV) {
        return employeeService.getEmployeeByMaNV(maNV)
                .map(e -> ResponseEntity.ok(new EmployeeDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Danh sách tất cả nhân viên */
    @GetMapping("/list")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> getEmployeeList() {
        List<EmployeeDto> list = employeeService.getAllEmployees().stream()
                .map(EmployeeDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /** Lấy thông tin cá nhân của nhân viên đang đăng nhập */
    @GetMapping("/me")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getMyProfile() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return employeeService.getEmployee(userDetails.getId())
                .map(e -> ResponseEntity.ok(new EmployeeDto(e)))
                .orElse(ResponseEntity.notFound().build());
    }
}
