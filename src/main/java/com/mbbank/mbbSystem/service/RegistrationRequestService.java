package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.dto.RegistrationRequestDto;
import com.mbbank.mbbSystem.model.*;
import com.mbbank.mbbSystem.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class RegistrationRequestService {

    @Autowired
    private AccountRegistrationRequestRepository requestRepo;

    @Autowired
    private BranchRepository branchRepo;

    @Autowired
    private EmployeeRepository employeeRepo;

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private AccountRepository accountRepo;

    @Autowired
    private CustomerRepository customerRepo;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ===================== PUBLIC: Khách gửi yêu cầu =====================

    /**
     * Khách hàng gửi yêu cầu tạo tài khoản (không cần đăng nhập).
     */
    @Transactional
    public RegistrationRequestDto submitRequest(RegistrationRequestDto dto) {
        // Ràng buộc dữ liệu: SĐT làm username
        String username = dto.getPhone();
        if (username == null || username.length() < 10) {
            throw new RuntimeException("Số điện thoại không hợp lệ (tối thiểu 10 số)!");
        }
        
        // Kiểm tra CCCD (ví dụ 12 số)
        if (dto.getCccd() == null || dto.getCccd().length() != 12) {
            throw new RuntimeException("Số CCCD phải đủ 12 chữ số!");
        }

        // Kiểm tra username trùng trong users và trong requests đang PENDING
        if (userRepo.existsByUsername(username))
            throw new RuntimeException("Số điện thoại này đã được đăng ký tài khoản!");
        if (requestRepo.existsByDesiredUsername(username))
            throw new RuntimeException("Số điện thoại này đang có yêu cầu chờ xử lý!");

        // Kiểm tra CCCD đã có tài khoản PENDING chưa
        requestRepo.findByCccdAndStatus(dto.getCccd(), "PENDING")
                .ifPresent(r -> { throw new RuntimeException("CCCD này đã có yêu cầu đang chờ xử lý!"); });

        // Kiểm tra CCCD đã tồn tại trong hệ thống chưa
        if (customerRepo.existsByCccd(dto.getCccd())) {
            throw new RuntimeException("Số CCCD này đã được đăng ký trên hệ thống!");
        }

        Branch branch = branchRepo.findById(dto.getBranchId())
                .orElseThrow(() -> new RuntimeException("Chi nhánh không tồn tại!"));

        AccountRegistrationRequest req = new AccountRegistrationRequest();
        req.setFullName(dto.getFullName());
        req.setCccd(dto.getCccd());
        req.setPhone(dto.getPhone());
        req.setEmail(dto.getEmail());
        req.setNgaySinh(dto.getNgaySinh());
        req.setAddress(dto.getAddress());
        req.setDesiredUsername(username); // Force phone as username
        req.setPassword(dto.getPassword()); 
        req.setBranch(branch);
        req.setLoaiTK(dto.getLoaiTK() != null ? dto.getLoaiTK() : "THANH_TOAN");
        req.setStatus("PENDING");
        req.setSubmittedAt(LocalDateTime.now());

        return toDto(requestRepo.save(req));
    }

    // ===================== EMPLOYEE: Xem & Duyệt yêu cầu =====================

    public List<RegistrationRequestDto> getRequestsByBranch(Long branchId, String status) {
        List<AccountRegistrationRequest> list;
        if (status != null && !status.isBlank()) {
            list = requestRepo.findByBranchIdAndStatus(branchId, status.toUpperCase());
        } else {
            list = requestRepo.findByBranchId(branchId);
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    public List<RegistrationRequestDto> getAllRequests(String status) {
        List<AccountRegistrationRequest> list;
        if (status != null && !status.isBlank()) {
            list = requestRepo.findByStatus(status.toUpperCase());
        } else {
            list = requestRepo.findAll();
        }
        return list.stream().map(this::toDto).collect(Collectors.toList());
    }

    @Transactional
    public RegistrationRequestDto approveRequest(Long requestId, Long employeeId) {
        AccountRegistrationRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại!"));

        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Yêu cầu đã được xử lý trước đó!");

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));

        if (employee.getBranch() == null || !employee.getBranch().getId().equals(req.getBranch().getId()))
            throw new RuntimeException("Bạn chỉ được duyệt yêu cầu của chi nhánh mình quản lý!");

        if (userRepo.existsByUsername(req.getDesiredUsername()))
            throw new RuntimeException("Tên đăng nhập (SĐT) đã bị trùng, không thể duyệt!");

        Customer customer = new Customer();
        customer.setUsername(req.getDesiredUsername());
        customer.setPassword(passwordEncoder.encode(req.getPassword()));
        customer.setFullName(req.getFullName());
        customer.setEmail(req.getEmail());
        customer.setMaKH(generateMaKH());
        customer.setNgaySinh(req.getNgaySinh());
        customer.setCccd(req.getCccd());
        customer.setPhone(req.getPhone());
        customer.setAddress(req.getAddress());
        customer.setStatus("ACTIVE");
        customer.setRole("ROLE_CUSTOMER");
        customer.setBranch(req.getBranch());
        userRepo.save(customer);

        String accNumber = req.getPhone(); // Dùng số điện thoại làm số tài khoản
        if (accountRepo.existsByAccountNumber(accNumber)) {
            // Nếu STK (SĐT) đã tồn tại, sinh ngẫu nhiên như cũ hoặc báo lỗi
            // Ở đây ta báo lỗi vì yêu cầu là "Lấy SĐT làm STK luôn"
            throw new RuntimeException("Số tài khoản (SĐT) " + accNumber + " đã tồn tại trên hệ thống!");
        }
        
        Account account = new Account(accNumber, BigDecimal.ZERO, req.getLoaiTK(), LocalDate.now(), customer);
        accountRepo.save(account);

        req.setStatus("APPROVED");
        req.setReviewedAt(LocalDateTime.now());
        req.setReviewedBy(employee);

        return toDto(requestRepo.save(req));
    }

    @Transactional
    public RegistrationRequestDto rejectRequest(Long requestId, Long employeeId, String reason) {
        AccountRegistrationRequest req = requestRepo.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Yêu cầu không tồn tại!"));

        if (!"PENDING".equals(req.getStatus()))
            throw new RuntimeException("Yêu cầu đã được xử lý trước đó!");

        Employee employee = employeeRepo.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Nhân viên không tồn tại!"));

        if (employee.getBranch() == null || !employee.getBranch().getId().equals(req.getBranch().getId()))
            throw new RuntimeException("Bạn chỉ được từ chối yêu cầu của chi nhánh mình quản lý!");

        req.setStatus("REJECTED");
        req.setRejectReason(reason);
        req.setReviewedAt(LocalDateTime.now());
        req.setReviewedBy(employee);

        return toDto(requestRepo.save(req));
    }

    private RegistrationRequestDto toDto(AccountRegistrationRequest r) {
        RegistrationRequestDto dto = new RegistrationRequestDto();
        dto.setId(r.getId());
        dto.setFullName(r.getFullName());
        dto.setCccd(r.getCccd());
        dto.setPhone(r.getPhone());
        dto.setEmail(r.getEmail());
        dto.setNgaySinh(r.getNgaySinh());
        dto.setAddress(r.getAddress());
        dto.setDesiredUsername(r.getDesiredUsername());
        if (r.getBranch() != null) {
            dto.setBranchId(r.getBranch().getId());
            dto.setBranchName(r.getBranch().getBranchName());
            dto.setMaChiNhanh(r.getBranch().getMaChiNhanh());
        }
        dto.setLoaiTK(r.getLoaiTK());
        dto.setStatus(r.getStatus());
        dto.setRejectReason(r.getRejectReason());
        dto.setSubmittedAt(r.getSubmittedAt());
        dto.setReviewedAt(r.getReviewedAt());
        if (r.getReviewedBy() != null) {
            dto.setReviewedByName(r.getReviewedBy().getFullName());
        }
        return dto;
    }

    private String generateMaKH() {
        return "KH" + String.format("%06d", new Random().nextInt(999999));
    }

    private String generateAccountNumber() {
        return "MB" + String.format("%010d", (long)(Math.random() * 9_000_000_000L) + 1_000_000_000L);
    }
}
