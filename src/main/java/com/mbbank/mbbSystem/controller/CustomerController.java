package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.CustomerDto;
import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

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
                .map(c -> ResponseEntity.ok(new CustomerDto(c)))
                .orElse(ResponseEntity.notFound().build());
    }

    /** Danh sách tất cả khách hàng */
    @GetMapping("/list")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getCustomerList() {
        List<CustomerDto> list = customerService.getAllCustomers().stream()
                .map(CustomerDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }
}
