package com.mbbank.mbbSystem.dto;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Customer;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * DTO đại diện cho Customer, expose toàn bộ trường nghiệp vụ nhưng KHÔNG expose password.
 * Bao gồm luôn thông tin tài khoản ngân hàng (mỗi KH chỉ có 1 TK duy nhất).
 */
public class CustomerDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String status;
    private String maKH;
    private LocalDate ngaySinh;
    private String cccd;
    private String phone;
    private String address;
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private String maChiNhanh;

    // === Thông tin tài khoản ngân hàng (gộp từ AccountDto) ===
    private Long accountId;
    private String accountNumber;
    private BigDecimal balance;
    private String loaiTK;
    private LocalDate ngayMo;
    private String accountStatus; // Trạng thái TK: HOAT_DONG, DA_KHOA

    public CustomerDto() {}

    public CustomerDto(Customer c) {
        this.id       = c.getId();
        this.username = c.getUsername();
        this.fullName = c.getFullName();
        this.email    = c.getEmail();
        this.role     = c.getRole();
        this.status   = c.getStatus();
        this.maKH     = c.getMaKH();
        this.ngaySinh = c.getNgaySinh();
        this.cccd     = c.getCccd();
        this.phone    = c.getPhone();
        this.address  = c.getAddress();
        if (c.getBranch() != null) {
            this.branchId   = c.getBranch().getId();
            this.branchName = c.getBranch().getBranchName();
            this.branchAddress = c.getBranch().getBranchAddress();
            this.maChiNhanh = c.getBranch().getMaChiNhanh();
        }
        // Gộp thông tin tài khoản — mỗi KH chỉ có 1 TK duy nhất
        if (c.getAccounts() != null && !c.getAccounts().isEmpty()) {
            Account acc = c.getAccounts().get(0);
            this.accountId     = acc.getId();
            this.accountNumber = acc.getAccountNumber();
            this.balance       = acc.getBalance();
            this.loaiTK        = acc.getLoaiTK();
            this.ngayMo        = acc.getNgayMo();
            this.accountStatus = acc.getStatus();
        }
    }

    // Getters
    public Long getId()           { return id; }
    public String getUsername()   { return username; }
    public String getFullName()   { return fullName; }
    public String getEmail()      { return email; }
    public String getRole()       { return role; }
    public String getStatus()     { return status; }
    public String getMaKH()       { return maKH; }
    public LocalDate getNgaySinh(){ return ngaySinh; }
    public String getCccd()       { return cccd; }
    public String getPhone()      { return phone; }
    public String getAddress()    { return address; }
    public Long getBranchId()     { return branchId; }
    public String getBranchName() { return branchName; }
    public String getBranchAddress() { return branchAddress; }
    public String getMaChiNhanh() { return maChiNhanh; }

    public Long getAccountId()        { return accountId; }
    public String getAccountNumber()  { return accountNumber; }
    public BigDecimal getBalance()    { return balance; }
    public String getLoaiTK()         { return loaiTK; }
    public LocalDate getNgayMo()      { return ngayMo; }
    public String getAccountStatus()  { return accountStatus; }

    // Setters
    public void setId(Long v)            { this.id = v; }
    public void setUsername(String v)    { this.username = v; }
    public void setFullName(String v)    { this.fullName = v; }
    public void setEmail(String v)       { this.email = v; }
    public void setRole(String v)        { this.role = v; }
    public void setStatus(String v)      { this.status = v; }
    public void setMaKH(String v)        { this.maKH = v; }
    public void setNgaySinh(LocalDate v) { this.ngaySinh = v; }
    public void setCccd(String v)        { this.cccd = v; }
    public void setPhone(String v)       { this.phone = v; }
    public void setAddress(String v)     { this.address = v; }
    public void setBranchId(Long v)      { this.branchId = v; }
    public void setBranchName(String v)  { this.branchName = v; }
    public void setBranchAddress(String v) { this.branchAddress = v; }
    public void setMaChiNhanh(String v)  { this.maChiNhanh = v; }

    public void setAccountId(Long v)        { this.accountId = v; }
    public void setAccountNumber(String v)  { this.accountNumber = v; }
    public void setBalance(BigDecimal v)    { this.balance = v; }
    public void setLoaiTK(String v)         { this.loaiTK = v; }
    public void setNgayMo(LocalDate v)      { this.ngayMo = v; }
    public void setAccountStatus(String v)  { this.accountStatus = v; }
}
