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
}
