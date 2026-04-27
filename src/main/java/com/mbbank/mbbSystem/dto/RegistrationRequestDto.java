package com.mbbank.mbbSystem.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * DTO cho yêu cầu đăng ký tài khoản (gửi từ frontend public, không cần JWT).
 * Dùng cho cả chiều gửi (request) lẫn chiều đọc (response).
 */
public class RegistrationRequestDto {

    // ID (chỉ dùng khi trả về)
    private Long id;

    // Thông tin cá nhân
    private String fullName;
    private String cccd;
    private String phone;
    private String email;
    private LocalDate ngaySinh;
    private String address;

    // Tài khoản đăng nhập mong muốn
    private String desiredUsername;
    private String password; // chỉ nhận từ form, KHÔNG trả ra response

    // Chi nhánh
    private Long branchId;
    private String branchName;  // trả về cho frontend
    private String maChiNhanh;  // trả về cho frontend

    // Loại TK
    private String loaiTK;

    // Trạng thái (trả về)
    private String status;
    private String rejectReason;
    private LocalDateTime submittedAt;
    private LocalDateTime reviewedAt;
    private String reviewedByName;

    public RegistrationRequestDto() {}

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

    public Long getBranchId() { return branchId; }
    public void setBranchId(Long branchId) { this.branchId = branchId; }

    public String getBranchName() { return branchName; }
    public void setBranchName(String branchName) { this.branchName = branchName; }

    public String getMaChiNhanh() { return maChiNhanh; }
    public void setMaChiNhanh(String maChiNhanh) { this.maChiNhanh = maChiNhanh; }

    public String getLoaiTK() { return loaiTK; }
    public void setLoaiTK(String loaiTK) { this.loaiTK = loaiTK; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getRejectReason() { return rejectReason; }
    public void setRejectReason(String rejectReason) { this.rejectReason = rejectReason; }

    public LocalDateTime getSubmittedAt() { return submittedAt; }
    public void setSubmittedAt(LocalDateTime submittedAt) { this.submittedAt = submittedAt; }

    public LocalDateTime getReviewedAt() { return reviewedAt; }
    public void setReviewedAt(LocalDateTime reviewedAt) { this.reviewedAt = reviewedAt; }

    public String getReviewedByName() { return reviewedByName; }
    public void setReviewedByName(String reviewedByName) { this.reviewedByName = reviewedByName; }
}
