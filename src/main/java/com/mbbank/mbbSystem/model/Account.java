package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "accounts", uniqueConstraints = { @UniqueConstraint(columnNames = "accountNumber") })
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String accountNumber;
    private BigDecimal balance;
    private String loaiTK; // Loại tài khoản: THANH_TOAN, TIET_KIEM,...
    private LocalDate ngayMo;  // Ngày mở tài khoản
    private String status = "HOAT_DONG"; // HOAT_DONG, DA_KHOA

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnore
    private Customer customer;

    @OneToMany(mappedBy = "fromAccount", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> sentTransactions;

    @OneToMany(mappedBy = "toAccount", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Transaction> receivedTransactions;

    public Account() {}

    public Account(String accountNumber, BigDecimal balance, String loaiTK, LocalDate ngayMo, Customer customer) {
        this.accountNumber = accountNumber;
        this.balance = balance;
        this.loaiTK = loaiTK;
        this.ngayMo = ngayMo;
        this.customer = customer;
    }

    public boolean kiemTraSoDu(BigDecimal amount) {
        return this.balance.compareTo(amount) >= 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public void setBalance(BigDecimal balance) { this.balance = balance; }
    public String getLoaiTK() { return loaiTK; }
    public void setLoaiTK(String loaiTK) { this.loaiTK = loaiTK; }
    public LocalDate getNgayMo() { return ngayMo; }
    public void setNgayMo(LocalDate ngayMo) { this.ngayMo = ngayMo; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public List<Transaction> getSentTransactions() { return sentTransactions; }
    public void setSentTransactions(List<Transaction> sentTransactions) { this.sentTransactions = sentTransactions; }
    public List<Transaction> getReceivedTransactions() { return receivedTransactions; }
    public void setReceivedTransactions(List<Transaction> receivedTransactions) { this.receivedTransactions = receivedTransactions; }
}
