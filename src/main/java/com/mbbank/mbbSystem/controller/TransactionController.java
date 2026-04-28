package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.MessageResponse;
import com.mbbank.mbbSystem.dto.TransferRequest;
import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Transaction;
import com.mbbank.mbbSystem.repository.AccountRepository;
import com.mbbank.mbbSystem.service.TransactionService;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transaction")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private AccountRepository accountRepository;

    @PostMapping("/transfer")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> transferMoney(@RequestBody TransferRequest request) {
        UserDetailsImpl userDetails = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<Account> fromAccOpt = accountRepository.findByAccountNumber(request.getFromAccount());
        if (fromAccOpt.isEmpty() || !fromAccOpt.get().getCustomer().getId().equals(userDetails.getId())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Tài khoản gửi không hợp lệ hoặc không thuộc quyền sở hữu của bạn."));
        }

        try {
            transactionService.transfer(request.getFromAccount(), request.getToAccount(), request.getAmount(), request.getContent());
            return ResponseEntity.ok(new MessageResponse("Chuyển tiền thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/deposit")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> deposit(@RequestBody TransferRequest request) {
        String employeeName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            transactionService.deposit(request.getToAccount(), request.getAmount(), employeeName);
            return ResponseEntity.ok(new MessageResponse("Nạp tiền thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @PostMapping("/withdraw")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> withdraw(@RequestBody TransferRequest request) {
        String employeeName = SecurityContextHolder.getContext().getAuthentication().getName();
        try {
            transactionService.withdraw(request.getFromAccount(), request.getAmount(), employeeName);
            return ResponseEntity.ok(new MessageResponse("Rút tiền thành công!"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    @GetMapping("/history/{accountNumber}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getTransactionHistory(@PathVariable String accountNumber) {
        List<Transaction> transactions = transactionService.getHistory(accountNumber);
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getAllTransactions() {
        List<Transaction> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> findByMaGD(@RequestParam String maGD) {
        return transactionService.findByMaGD(maGD)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
