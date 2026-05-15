package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.model.Branch;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import com.mbbank.mbbSystem.service.BranchService;
import com.mbbank.mbbSystem.service.EmployeeService;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/branch")
public class BranchController {

    @Autowired
    private BranchService branchService;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeRepository employeeRepository;

    /** Danh sách tất cả chi nhánh — employee/sysadmin đều xem được */
    @GetMapping("/list")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    /**
     * Danh sách chi nhánh PUBLIC — cho trang đăng ký khách hàng (không cần JWT).
     */
    @GetMapping("/public/list")
    public ResponseEntity<?> getPublicBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    /** Chi nhánh theo id */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        return branchService.getBranch(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /** Tạo chi nhánh mới — chỉ sysadmin */
    @PostMapping("/add")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> create(@RequestBody Branch branch) {
        try {
            return ResponseEntity.ok(branchService.createBranch(branch));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Cập nhật chi nhánh */
    @PutMapping("/update/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Branch branch) {
        try {
            return ResponseEntity.ok(branchService.updateBranch(id, branch));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /** Xóa chi nhánh */
    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        try {
            branchService.deleteBranch(id);
            return ResponseEntity.ok("Xóa chi nhánh thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Phân công nhân viên vào chi nhánh — SysAdmin.
     * PUT /api/branch/assign-employee
     * Body: { "employeeId": 1, "branchId": 2 }
     */
    @PutMapping("/assign-employee")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> assignEmployee(@RequestBody Map<String, Long> body) {
        Long employeeId = body.get("employeeId");
        Long branchId   = body.get("branchId");
        if (employeeId == null || branchId == null)
            return ResponseEntity.badRequest().body("Thiếu employeeId hoặc branchId");
        try {
            employeeService.assignBranch(employeeId, branchId);
            return ResponseEntity.ok("Phân công chi nhánh thành công");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Bổ sung quỹ tiền mặt cho chi nhánh - Dành cho Quản lý chi nhánh
     */
    @PostMapping("/add-fund")
    @PreAuthorize("hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> addFund(@RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            return ResponseEntity.badRequest().body("Số tiền bổ sung không hợp lệ");
        }

        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee employee = employeeRepository.findById(userDetails.getId()).orElse(null);
        if (employee == null || employee.getBranch() == null) {
            return ResponseEntity.badRequest().body("Không xác định được chi nhánh của bạn");
        }

        try {
            Branch branch = employee.getBranch();
            branch.setCashFund(branch.getCashFund().add(amount));
            branchService.updateBranch(branch.getId(), branch);
            return ResponseEntity.ok(Map.of("message", "Bổ sung quỹ thành công", "newFund", branch.getCashFund()));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
