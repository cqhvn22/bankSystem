package com.mbbank.mbbSystem.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Lưu yêu cầu mở tài khoản từ khách hàng (self-registration).
 * Khi gửi lên: status = PENDING
 * Nhân viên chi nhánh duyệt: status = APPROVED (→ tạo Customer + Account)
 * Từ chối: status = REJECTED
 */
@Entity
@Table(name = "account_registration_requests")
public class AccountRegistrationRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // --- Thông tin cá nhân ---
    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String fullName;

    @Column(nullable = false, unique = true)
    private String cccd;

    @Column(nullable = false)
    private String phone;

    private String email;

    private LocalDate ngaySinh;

    @Column(columnDefinition = "NVARCHAR(500)")
    private String address;

    // --- Tài khoản đăng nhập mong muốn ---
    @Column(nullable = false, unique = true)
    private String desiredUsername;

    @Column(nullable = false)
    private String password; // sẽ được encode khi duyệt

    // --- Chi nhánh quản lý ---
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "branch_id", nullable = false)
    private Branch branch;

    // --- Trạng thái ---
    @Column(nullable = false)
    private String status = "PENDING"; // PENDING | APPROVED | REJECTED

    @Column(columnDefinition = "NVARCHAR(500)")
    private String rejectReason;

    private LocalDateTime submittedAt = LocalDateTime.now();
    private LocalDateTime reviewedAt;

    // Nhân viên đã duyệt (nullable khi PENDING)
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "reviewed_by")
    private Employee reviewedBy;

    // Loại tài khoản mong muốn
    @Column(columnDefinition = "NVARCHAR(50)")
    private String loaiTK = "THANH_TOAN";

    public AccountRegistrationRequest() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public String getCccd() { return cccd; }
    public void setCccd(String cccd) { this.cccd = cccd; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDate getNgaySinh() { return ngaySinh; }
    public void setNgaySinh(LocalDate ngaySinh) { this.ngaySinh = ngaySinh; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getDesiredUsername() { return desiredUsername; }
    public void setDesiredUsername(String desiredUsername) { this.desiredUsername = desiredUsername; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public Branch getBranch() { return branch; }
    public void setBranch(Branch branch) { this.branch = branch; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public Employee getReviewedBy() { return reviewedBy; }
    public void setReviewedBy(Employee reviewedBy) { this.reviewedBy = reviewedBy; }

    public String getLoaiTK() { return loaiTK; }
    public void setLoaiTK(String loaiTK) { this.loaiTK = loaiTK; }
}
