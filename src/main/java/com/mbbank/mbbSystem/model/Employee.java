package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "employees")
public class Employee extends User {

    @Column(unique = true)
    private String maNV; // Mã nhân viên nghiệp vụ

    @Column(columnDefinition = "NVARCHAR(255)")
    private String position; // Chức vụ (chucVu)

    @Column(columnDefinition = "NVARCHAR(255)")
    private String boPhan; // Bộ phận

    private BigDecimal luong; // Lương (double -> BigDecimal)

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer","handler"})
    private Branch branch;

    public Employee() {
        super();
        this.setRole("ROLE_EMPLOYEE");
    }

    public Employee(String username, String password, String fullName, String email, String maNV, String position, String boPhan, BigDecimal luong, Branch branch) {
        super(username, password, fullName, email, "ROLE_EMPLOYEE");
        this.maNV = maNV;
        this.position = position;
        this.boPhan = boPhan;
        this.luong = luong;
        this.branch = branch;
    }

    public boolean xacMinhKYC(Customer customer) {
        // Logic xác minh danh tính khách hàng (KYC)
        return customer != null && customer.getCccd() != null && !customer.getCccd().isEmpty();
    }

    // Getters and Setters
    public String getMaNV() { return maNV; }
    public void setMaNV(String maNV) { this.maNV = maNV; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getBoPhan() { return boPhan; }
    public void setBoPhan(String boPhan) { this.boPhan = boPhan; }
    public BigDecimal getLuong() { return luong; }
    public void setLuong(BigDecimal luong) { this.luong = luong; }
    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }
}
