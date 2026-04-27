package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Transaction;
import com.mbbank.mbbSystem.repository.AccountRepository;
import com.mbbank.mbbSystem.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Transactional
    public Transaction deposit(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);

        String maGD = generateMaGD("NAP");
        Transaction transaction = new Transaction(maGD, null, account, amount, LocalDateTime.now(), "DEPOSIT", "DEPOSIT");
        transaction.ghiNhatKyGD();
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction withdraw(String accountNumber, BigDecimal amount) {
        Account account = accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new RuntimeException("Account not found"));
        
        if (!account.kiemTraSoDu(amount)) {
            throw new RuntimeException("Khong du so du");
        }

        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);

        String maGD = generateMaGD("RUT");
        Transaction transaction = new Transaction(maGD, account, null, amount, LocalDateTime.now(), "WITHDRAW", "WITHDRAW");
        transaction.ghiNhatKyGD();
        return transactionRepository.save(transaction);
    }

    @Transactional
    public Transaction transfer(String fromAccountNum, String toAccountNum, BigDecimal amount, String content) {
        Account fromAccount = accountRepository.findByAccountNumber(fromAccountNum).orElseThrow(() -> new RuntimeException("Source account not found"));
        Account toAccount = accountRepository.findByAccountNumber(toAccountNum).orElseThrow(() -> new RuntimeException("Destination account not found"));

        if (!fromAccount.kiemTraSoDu(amount)) {
            throw new RuntimeException("Khong du so du");
        }

        fromAccount.setBalance(fromAccount.getBalance().subtract(amount));
        toAccount.setBalance(toAccount.getBalance().add(amount));

        accountRepository.save(fromAccount);
        accountRepository.save(toAccount);

        String maGD = generateMaGD("CK");
        Transaction transaction = new Transaction(maGD, fromAccount, toAccount, amount, LocalDateTime.now(), content, "TRANSFER");
        transaction.ghiNhatKyGD();
        return transactionRepository.save(transaction);
    }

    public List<Transaction> getHistory(String accountNumber) {
        return transactionRepository.findByFromAccount_AccountNumberOrToAccount_AccountNumberOrderByTimestampDesc(accountNumber, accountNumber);
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAllByOrderByTimestampDesc();
    }

    public Optional<Transaction> findByMaGD(String maGD) {
        return transactionRepository.findByMaGD(maGD);
    }

    /** Sinh mã giao dịch: loại + yyyyMMddHHmmssSSS */
    private String generateMaGD(String loai) {
        String ts = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        return loai + ts;
    }
}
