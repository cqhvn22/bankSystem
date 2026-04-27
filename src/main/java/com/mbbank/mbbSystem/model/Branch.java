package com.mbbank.mbbSystem.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "branches")
public class Branch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String maChiNhanh; // Mã chi nhánh nghiệp vụ

    @Column(columnDefinition = "NVARCHAR(255)")
    private String branchName;   // tenChiNhanh

    @Column(columnDefinition = "NVARCHAR(255)")
    private String branchAddress; // diaChi

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Employee> employees;

    public Branch() {}

    public Branch(String maChiNhanh, String branchName, String branchAddress) {
        this.maChiNhanh = maChiNhanh;
        this.branchName = branchName;
        this.branchAddress = branchAddress;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(String maChiNhanh) { this.maChiNhanh = maChiNhanh; }
    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }
    public String getBranchAddress() { return branchAddress; }
    public void setBranchAddress(String branchAddress) { this.branchAddress = branchAddress; }
    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}
