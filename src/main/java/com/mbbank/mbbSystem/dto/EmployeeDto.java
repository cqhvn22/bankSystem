package com.mbbank.mbbSystem.dto;

import com.mbbank.mbbSystem.model.Employee;

import java.math.BigDecimal;

/**
 * DTO đại diện cho Employee — không expose password, tránh lazy load Branch.
 */
public class EmployeeDto {
    private Long id;
    private String username;
    private String fullName;
    private String email;
    private String role;
    private String status;
    private String maNV;
    private String position;
    private String boPhan;
    private BigDecimal luong;
    private Long branchId;
    private String branchName;
    private String branchAddress;
    private BigDecimal branchCashFund;

    public EmployeeDto() {}

    public EmployeeDto(Employee e) {
        this.id       = e.getId();
        this.username = e.getUsername();
        this.fullName = e.getFullName();
        this.email    = e.getEmail();
        this.role     = e.getRole();
        this.status   = e.getStatus();
        this.maNV     = e.getMaNV();
        this.position = e.getPosition();
        this.boPhan   = e.getBoPhan();
        this.luong    = e.getLuong();
        if (e.getBranch() != null) {
            this.branchId      = e.getBranch().getId();
            this.branchName    = e.getBranch().getBranchName();
            this.branchAddress = e.getBranch().getBranchAddress();
            this.branchCashFund = e.getBranch().getCashFund();
        }
    }

    // Getters
    public Long getId()         { return id; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getEmail()    { return email; }
    public String getRole()     { return role; }
    public String getStatus()   { return status; }
    public String getMaNV()     { return maNV; }
    public String getPosition() { return position; }
    public String getBoPhan()   { return boPhan; }
    public BigDecimal getLuong(){ return luong; }
    public Long getBranchId()   { return branchId; }
    public String getBranchName()    { return branchName; }
    public String getBranchAddress() { return branchAddress; }
    public BigDecimal getBranchCashFund() { return branchCashFund; }

    // Setters (needed for Jackson deserialization from request body)
    public void setId(Long id)              { this.id = id; }
    public void setUsername(String v)        { this.username = v; }
    public void setFullName(String v)        { this.fullName = v; }
    public void setEmail(String v)           { this.email = v; }
    public void setRole(String v)            { this.role = v; }
    public void setStatus(String v)          { this.status = v; }
    public void setMaNV(String v)            { this.maNV = v; }
    public void setPosition(String v)        { this.position = v; }
    public void setBoPhan(String v)          { this.boPhan = v; }
    public void setLuong(BigDecimal v)       { this.luong = v; }
    public void setBranchId(Long v)          { this.branchId = v; }
    public void setBranchName(String v)      { this.branchName = v; }
    public void setBranchAddress(String v)   { this.branchAddress = v; }
    public void setBranchCashFund(BigDecimal v) { this.branchCashFund = v; }
}
