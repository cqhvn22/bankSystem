package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String maGD; // Mã giao dịch nghiệp vụ

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_account_id")
    @JsonIgnoreProperties({"sentTransactions", "receivedTransactions", "customer", "hibernateLazyInitializer", "handler"})
    private Account fromAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_account_id")
    @JsonIgnoreProperties({"sentTransactions", "receivedTransactions", "customer", "hibernateLazyInitializer", "handler"})
    private Account toAccount;

    private BigDecimal amount;
    private LocalDateTime timestamp;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String content;

    private String transactionType; // DEPOSIT, WITHDRAW, TRANSFER

    private String performedBy; // Username of employee who performed this at counter

    public Transaction() {}

    public Transaction(String maGD, Account fromAccount, Account toAccount, BigDecimal amount, LocalDateTime timestamp, String content, String transactionType, String performedBy) {
        this.maGD = maGD;
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.timestamp = timestamp;
        this.content = content;
        this.transactionType = transactionType;
        this.performedBy = performedBy;
    }

    public void ghiNhatKyGD() {
        // Mock implement for ghiNhatKyGD
        this.timestamp = LocalDateTime.now();
        System.out.println("Recorded transaction: " + this.transactionType + " amount: " + this.amount);
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaGD() { return maGD; }
    public void setMaGD(String maGD) { this.maGD = maGD; }
    public Account getFromAccount() { return fromAccount; }
    public void setFromAccount(Account fromAccount) { this.fromAccount = fromAccount; }
    public Account getToAccount() { return toAccount; }
    public void setToAccount(Account toAccount) { this.toAccount = toAccount; }
    public BigDecimal getAmount() { return amount; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public String getTransactionType() { return transactionType; }
    public void setTransactionType(String transactionType) { this.transactionType = transactionType; }

    public String getPerformedBy() { return performedBy; }
    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }
}
