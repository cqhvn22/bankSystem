package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String maChiNhanh;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String branchName;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String branchAddress;

    /** Quỹ tiền mặt tại quầy của chi nhánh */
    @Column(precision = 18, scale = 2, nullable = false, columnDefinition = "DECIMAL(18,2) DEFAULT 0")
    private BigDecimal cashFund = BigDecimal.ZERO;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Employee> employees;

    public Branch() {}

    public Branch(String maChiNhanh, String branchName, String branchAddress) {
        this.maChiNhanh = maChiNhanh;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
        this.cashFund = BigDecimal.ZERO;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(String maChiNhanh) { this.maChiNhanh = maChiNhanh; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getBranchAddress() { return branchAddress; }
    public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
    public BigDecimal getCashFund() { return cashFund != null ? cashFund : BigDecimal.ZERO; }
    public void setCashFund(BigDecimal cashFund) { this.cashFund = cashFund; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
