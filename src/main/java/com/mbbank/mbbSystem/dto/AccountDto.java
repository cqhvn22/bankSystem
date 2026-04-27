package com.mbbank.mbbSystem.dto;

import com.mbbank.mbbSystem.model.Account;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDto {
    private Long id;
    private String accountNumber;
    private BigDecimal balance;
    private String loaiTK;
    private LocalDate ngayMo;
    private String status;
    private Long customerId;
    private String customerName;
    private String maKH;

    public AccountDto(Account account) {
        this.id = account.getId();
        this.accountNumber = account.getAccountNumber();
        this.balance = account.getBalance();
        this.loaiTK = account.getLoaiTK();
        this.ngayMo = account.getNgayMo();
        this.status = account.getStatus();
        if (account.getCustomer() != null) {
            this.customerId   = account.getCustomer().getId();
            this.customerName = account.getCustomer().getFullName();
            // getMaKH() only exists on Customer subclass — safe check for Java 17
            if (account.getCustomer() instanceof com.mbbank.mbbSystem.model.Customer) {
                this.maKH = ((com.mbbank.mbbSystem.model.Customer) account.getCustomer()).getMaKH();
            }
        }
    }

    public Long getId() { return id; }
    public String getAccountNumber() { return accountNumber; }
    public BigDecimal getBalance() { return balance; }
    public String getLoaiTK() { return loaiTK; }
    public LocalDate getNgayMo() { return ngayMo; }
    public String getStatus() { return status; }
    public Long getCustomerId() { return customerId; }
    public String getCustomerName() { return customerName; }
    public String getMaKH() { return maKH; }
}
