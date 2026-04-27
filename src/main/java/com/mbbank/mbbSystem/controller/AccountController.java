package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.service.AccountService;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/account")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @GetMapping("/my-accounts")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getMyAccounts() {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<com.mbbank.mbbSystem.dto.AccountDto> dtos = accountService.getCustomerAccounts(userDetails.getId()).stream()
                .map(com.mbbank.mbbSystem.dto.AccountDto::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    @PostMapping("/create")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
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
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> lockAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.lockAccount(accountId);
            return ResponseEntity.ok(new com.mbbank.mbbSystem.dto.AccountDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/unlock/{accountId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> unlockAccount(@PathVariable Long accountId) {
        try {
            Account account = accountService.unlockAccount(accountId);
            return ResponseEntity.ok(new com.mbbank.mbbSystem.dto.AccountDto(account));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> findAccount(@RequestParam String accountNumber) {
        return accountService.findAccount(accountNumber)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/check")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> checkAccountName(@RequestParam String accountNumber) {
        return accountService.findAccount(accountNumber)
                .map(acc -> ResponseEntity.ok(Map.of("customerName", acc.getCustomer() != null ? acc.getCustomer().getFullName() : "Tài khoản hệ thống")))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getAllAccounts() {
        List<com.mbbank.mbbSystem.dto.AccountDto> dtos = accountService.getAllAccounts().stream()
                .map(com.mbbank.mbbSystem.dto.AccountDto::new)
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(dtos);
    }
}
