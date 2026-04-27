package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.CustomerDto;
import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import com.mbbank.mbbSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeRepository employeeRepository;

    /** Thêm khách hàng (KHÔNG dùng password — dùng /api/auth/register-customer để tạo user+account) */
    @PostMapping("/add")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> addCustomer(@RequestBody CustomerDto req) {
        try {
            Customer customer = new Customer();
            customer.setUsername(req.getUsername());
            customer.setFullName(req.getFullName());
            customer.setEmail(req.getEmail());
            customer.setMaKH(req.getMaKH());
            if (req.getNgaySinh() != null) customer.setNgaySinh(req.getNgaySinh());
            customer.setCccd(req.getCccd());
            customer.setPhone(req.getPhone());
            customer.setAddress(req.getAddress());
            customer.setRole("ROLE_CUSTOMER");
            
            // Nếu là EMPLOYEE, ép chi nhánh của khách hàng là chi nhánh của nhân viên
            UserDetailsImpl userDetails = getCurrentUser();
            if (hasRole(userDetails, "ROLE_EMPLOYEE")) {
                Employee employee = getEmployee(userDetails.getId());
                customer.setBranch(employee.getBranch());
            }

            Customer saved = customerService.addCustomer(customer);
            return ResponseEntity.ok(new CustomerDto(saved));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Cập nhật thông tin khách hàng */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> updateCustomer(@PathVariable Long id, @RequestBody CustomerDto req) {
        try {
            Customer patch = new Customer();
            patch.setFullName(req.getFullName());
            patch.setPhone(req.getPhone());
            patch.setAddress(req.getAddress());
            patch.setCccd(req.getCccd());
            patch.setMaKH(req.getMaKH());
            patch.setNgaySinh(req.getNgaySinh());
            patch.setEmail(req.getEmail());

            // Bảo mật chi nhánh
            checkBranchPermission(id);

            Customer updated = customerService.updateCustomer(id, patch);
            return ResponseEntity.ok(new CustomerDto(updated));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * KHÔNG cung cấp endpoint xóa khách hàng.
     * Lý do: Xóa customer sẽ gây mất lịch sử giao dịch liên quan (orphan transactions).
     * Thay vào đó, sử dụng khóa khách hàng.
     */
    @PutMapping("/lock/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> lockCustomer(@PathVariable Long id) {
        try {
            checkBranchPermission(id);
            customerService.lockCustomer(id);
            return ResponseEntity.ok("Đã khóa tài khoản khách hàng.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/unlock/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> unlockCustomer(@PathVariable Long id) {
        try {
            checkBranchPermission(id);
            customerService.unlockCustomer(id);
            return ResponseEntity.ok("Đã mở khóa tài khoản khách hàng.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Tìm theo id */
    @GetMapping("/search/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getCustomer(@PathVariable Long id) {
        return customerService.getCustomer(id)
                .map(c -> ResponseEntity.ok(new CustomerDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Tìm theo mã KH */
    @GetMapping("/search")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> searchByMaKH(@RequestParam String maKH) {
        return customerService.getCustomerByMaKH(maKH)
                .map(c -> {
                    // Kiểm tra chi nhánh nếu là employee
                    try {
                        checkBranchPermission(c.getId());
                        return ResponseEntity.ok(new CustomerDto(c));
                    } catch (Exception e) {
                        return ResponseEntity.status(403).body(null);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Danh sách khách hàng:
     * - SYSADMIN: xem tất cả
     * - EMPLOYEE: chỉ xem KH thuộc chi nhánh mình
     */
    @GetMapping("/list")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getCustomerList() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        if ("ROLE_SYSADMIN".equals(role)) {
            // SysAdmin xem tất cả
            List<CustomerDto> list = customerService.getAllCustomers().stream()
                    .map(CustomerDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        } else {
            // Employee chỉ xem KH của chi nhánh mình
            Employee employee = employeeRepository.findById(userDetails.getId()).orElse(null);
            if (employee == null || employee.getBranch() == null) {
                return ResponseEntity.badRequest().body("Nhân viên chưa được gán chi nhánh!");
            }
            List<CustomerDto> list = customerService.getCustomersByBranch(employee.getBranch().getId())
                    .stream()
                    .map(CustomerDto::new)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(list);
        }
    }

    /** SysAdmin xem tất cả KH (không phân biệt chi nhánh) */
    @GetMapping("/list/all")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> getAllCustomers() {
        List<CustomerDto> list = customerService.getAllCustomers().stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    /**
     * Khách hàng lấy thông tin cá nhân của chính mình.
     */
    @GetMapping("/me")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getMyProfile() {
        UserDetailsImpl userDetails = getCurrentUser();
        return customerService.getCustomer(userDetails.getId())
                .map(c -> ResponseEntity.ok(new CustomerDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= HELPERS =================

    private UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private boolean hasRole(UserDetailsImpl user, String role) {
        return user.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals(role));
    }

    private Employee getEmployee(Long id) {
        return employeeRepository.findById(id).orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));
    }

    /**
     * Kiểm tra xem nhân viên hiện tại có quyền thao tác trên khách hàng này không.
     * (Phải cùng chi nhánh).
     */
    private void checkBranchPermission(Long customerId) {
        UserDetailsImpl userDetails = getCurrentUser();
        if (hasRole(userDetails, "ROLE_SYSADMIN")) return; // Admin có toàn quyền

        Employee employee = getEmployee(userDetails.getId());
        if (employee.getBranch() == null) throw new RuntimeException("Nhân viên chưa được gán chi nhánh!");

        Customer customer = customerService.getCustomer(customerId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy khách hàng!"));

        if (customer.getBranch() == null || !customer.getBranch().getId().equals(employee.getBranch().getId())) {
            throw new RuntimeException("Bạn không có quyền quản lý khách hàng của chi nhánh khác!");
        }
    }
}

