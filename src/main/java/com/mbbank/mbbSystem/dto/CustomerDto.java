package com.mbbank.mbbSystem.dto;

import com.mbbank.mbbSystem.model.Customer;

import java.time.LocalDate;

/**
 * DTO đại diện cho Customer, expose toàn bộ trường nghiệp vụ nhưng KHÔNG expose password.
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
}
