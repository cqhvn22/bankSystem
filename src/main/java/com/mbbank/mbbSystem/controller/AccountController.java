package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.service.AccountService;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @Autowired
    private EmployeeRepository employeeRepo;

    @GetMapping("/my-accounts")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> getMyAccounts() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<com.mbbank.mbbSystem.dto.AccountDto> dtos = accountService.getCustomerAccounts(userDetails.getId()).stream()
                .map(com.mbbank.mbbSystem.dto.AccountDto::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> createAccount(@RequestBody Map<String, Object> request) {
        String accountNumber = (String) request.get("accountNumber");
        BigDecimal initialBalance = new BigDecimal(request.get("initialBalance").toString());
        Long customerId = Long.valueOf(request.get("customerId").toString());
        String loaiTK = request.get("loaiTK") != null ? (String) request.get("loaiTK") : "THANH_TOAN";
        LocalDate ngayMo = request.get("ngayMo") != null
                ? LocalDate.parse(request.get("ngayMo").toString())
                : LocalDate.now();

        try {
            Account account = accountService.createAccount(accountNumber, initialBalance, loaiTK, ngayMo, customerId);
            return ResponseEntity.ok(account);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/lock/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> lockAccount(@PathVariable Long accountId) {
        try {
            checkBranchPermission(accountId);
            Account account = accountService.lockAccount(accountId);
            return ResponseEntity.ok(new com.mbbank.mbbSystem.dto.AccountDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/unlock/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> unlockAccount(@PathVariable Long accountId) {
        try {
            checkBranchPermission(accountId);
            Account account = accountService.unlockAccount(accountId);
            return ResponseEntity.ok(new com.mbbank.mbbSystem.dto.AccountDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> findAccount(@RequestParam String accountNumber) {
        return accountService.findAccount(accountNumber)
                .map(acc -> {
                    try {
                        checkBranchPermission(acc.getId());
                        return ResponseEntity.ok(acc);
                    } catch (Exception e) {
                        return ResponseEntity.status(403).body(null);
                    }
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> checkAccountName(@RequestParam String accountNumber) {
        return accountService.searchCounterAccount(accountNumber)
                .map(acc -> {
                    java.util.Map<String, Object> resp = new java.util.HashMap<>();
                    resp.put("customerName", acc.getCustomer() != null ? acc.getCustomer().getFullName() : "Tài khoản hệ thống");
                    resp.put("accountNumber", acc.getAccountNumber());
                    resp.put("balance", acc.getBalance());
                    resp.put("phone", acc.getCustomer() != null ? acc.getCustomer().getPhone() : "-");
                    resp.put("cccd", acc.getCustomer() != null ? acc.getCustomer().getCccd() : "-");
                    resp.put("branchName", (acc.getCustomer() != null && acc.getCustomer().getBranch() != null) ? acc.getCustomer().getBranch().getBranchName() : "-");
                    return ResponseEntity.ok(resp);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN') or hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> getAllAccounts() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();

        List<com.mbbank.mbbSystem.dto.AccountDto> list;
        if ("ROLE_SYSADMIN".equals(role)) {
            list = accountService.getAllAccounts().stream()
                    .map(com.mbbank.mbbSystem.dto.AccountDto::new)
                    .collect(Collectors.toList());
        } else {
            Employee employee = employeeRepo.findById(userDetails.getId()).orElse(null);
            if (employee == null || employee.getBranch() == null) {
                return ResponseEntity.badRequest().body("Nhân viên chưa được gán chi nhánh!");
            }
            list = accountService.getAccountsByBranch(employee.getBranch().getId()).stream()
                    .map(com.mbbank.mbbSystem.dto.AccountDto::new)
                    .collect(Collectors.toList());
        }
        return ResponseEntity.ok(list);
    }

    private void checkBranchPermission(Long accountId) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = userDetails.getAuthorities().iterator().next().getAuthority();
        if ("ROLE_SYSADMIN".equals(role)) return;
        if ("ROLE_BRANCH_MANAGER".equals(role)) {
            Employee employee = employeeRepo.findById(userDetails.getId())
                    .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));
            Account account = accountService.getAccountById(accountId)
                    .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));
            if (account.getCustomer() == null || account.getCustomer().getBranch() == null ||
                !account.getCustomer().getBranch().getId().equals(employee.getBranch().getId())) {
                throw new RuntimeException("Bạn không có quyền quản lý tài khoản của chi nhánh khác!");
            }
            return;
        }

        Employee employee = employeeRepo.findById(userDetails.getId())
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));
        
        Account account = accountService.getAccountById(accountId)
                .orElseThrow(() -> new RuntimeException("Tài khoản không tồn tại!"));

        if (account.getCustomer() == null || account.getCustomer().getBranch() == null || 
            !account.getCustomer().getBranch().getId().equals(employee.getBranch().getId())) {
            throw new RuntimeException("Bạn không có quyền quản lý tài khoản của chi nhánh khác!");
        }
    }
}
