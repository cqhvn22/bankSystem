package com.mbbank.mbbSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users", uniqueConstraints = { @UniqueConstraint(columnNames = "username") })
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;

    @Column(columnDefinition = "NVARCHAR(255)")
    private String fullName;
    private String email;
    private String role; // ROLE_CUSTOMER, ROLE_EMPLOYEE, ROLE_SYSADMIN
    private String status = "ACTIVE"; // ACTIVE, LOCKED

    public User() {}

    public User(String username, String password, String fullName, String email, String role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.email = email;
        this.role = role;
    }

    public boolean authenticateOTP(String otp) {
        // Mock implement for authenticateOTP as required in baocao.docx Use Case
        return otp != null && otp.equals("123456");
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}
