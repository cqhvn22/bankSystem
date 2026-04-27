package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.repository.AccountRepository;
import com.mbbank.mbbSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public Account createAccount(String accountNumber, BigDecimal initialBalance, String loaiTK, LocalDate ngayMo, Long customerId) {
        Customer customer = customerRepository.findById(customerId).orElseThrow(() -> new RuntimeException("Customer not found"));
        Account account = new Account(accountNumber, initialBalance, loaiTK, ngayMo != null ? ngayMo : LocalDate.now(), customer);
        return accountRepository.save(account);
    }

    public Account updateAccountInfo(Long accountId, Account newInfo) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        if (newInfo.getLoaiTK() != null) account.setLoaiTK(newInfo.getLoaiTK());
        if (newInfo.getNgayMo() != null) account.setNgayMo(newInfo.getNgayMo());
        if (newInfo.getStatus() != null) account.setStatus(newInfo.getStatus());
        return accountRepository.save(account);
    }

    public Account lockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("DA_KHOA");
        return accountRepository.save(account);
    }

    public Account unlockAccount(Long accountId) {
        Account account = accountRepository.findById(accountId).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setStatus("HOAT_DONG");
        return accountRepository.save(account);
    }

    public Optional<Account> findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public List<Account> getCustomerAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId);
    }

    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }
}
