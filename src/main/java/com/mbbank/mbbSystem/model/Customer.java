package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "customers")
public class Customer extends User {

    @Column(unique = true)
    private String maKH; // Mã khách hàng nghiệp vụ

    private LocalDate ngaySinh; // Ngày sinh

    @Column(unique = true)
    private String cccd;
    private String phone;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String address;

    /** Chi nhánh quản lý khách hàng này */
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler", "employees"})
    private Branch branch;

    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Account> accounts;

    public Customer() {
        super();
        this.setRole("ROLE_CUSTOMER");
    }

    public Customer(String username, String password, String fullName, String email, String maKH, LocalDate ngaySinh, String cccd, String phone, String address) {
        super(username, password, fullName, email, "ROLE_CUSTOMER");
        this.maKH = maKH;
        this.ngaySinh = ngaySinh;
        this.cccd = cccd;
        this.phone = phone;
        this.address = address;
    }

    // Getters and Setters
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
    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }
    public List<Account> getAccounts() { return accounts; }
    public void setAccounts(List<Account> accounts) { this.accounts = accounts; }
}
