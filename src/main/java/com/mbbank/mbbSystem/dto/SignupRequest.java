package com.mbbank.mbbSystem.dto;

import java.time.LocalDate;

public class SignupRequest {
    private String username;
    private String password;
    private String fullName;
    private String email;
    private String role;

    // Customer-specific fields
    private String maKH;
    private LocalDate ngaySinh;
    private String cccd;
    private String phone;
    private String address;

    // Employee-specific fields
    private String maNV;
    private String boPhan;
    private Double luong;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }
    public String getMaKH() { return maKH; }
    public void setMaKH(String maKH) { this.maKH = maKH; }
    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }
    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getBoPhan() { return boPhan; }
    public void setBoPhan(String boPhan) { this.boPhan = boPhan; }
    public Double getLuong() { return luong; }
    public void setLuong(Double luong) { this.luong = luong; }
}
