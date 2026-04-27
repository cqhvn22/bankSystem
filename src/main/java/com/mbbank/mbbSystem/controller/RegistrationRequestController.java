package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.MessageResponse;
import com.mbbank.mbbSystem.dto.RegistrationRequestDto;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import com.mbbank.mbbSystem.service.RegistrationRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/registration")
public class RegistrationRequestController {

    @Autowired
    private RegistrationRequestService service;

    @Autowired
    private EmployeeRepository employeeRepo;

    // ===================== PUBLIC (không cần JWT) =====================

    /**
     * Khách hàng gửi yêu cầu mở tài khoản.
     * Không yêu cầu xác thực.
     */
    @PostMapping("/submit")
    public ResponseEntity<?> submitRequest(@RequestBody RegistrationRequestDto dto) {
        try {
            RegistrationRequestDto result = service.submitRequest(dto);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // ===================== EMPLOYEE: Xem yêu cầu của chi nhánh mình =====================

    /**
     * Nhân viên xem danh sách yêu cầu của chi nhánh mình.
     * Query param ?status=PENDING|APPROVED|REJECTED (tùy chọn)
     */
    @GetMapping("/branch/list")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> getRequestsForMyBranch(@RequestParam(required = false) String status) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            Employee employee = getEmployeeFromUser(userDetails.getId());

            if (employee.getBranch() == null)
                return ResponseEntity.badRequest().body(new MessageResponse("Nhân viên chưa được gán chi nhánh!"));

            Long branchId = employee.getBranch().getId();
            List<RegistrationRequestDto> list = service.getRequestsByBranch(branchId, status);
            return ResponseEntity.ok(list);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * SYSADMIN xem tất cả yêu cầu (mọi chi nhánh).
     */
    @GetMapping("/all")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> getAllRequests(@RequestParam(required = false) String status) {
        try {
            return ResponseEntity.ok(service.getAllRequests(status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // ===================== EMPLOYEE: Duyệt / Từ chối =====================

    /**
     * Nhân viên DUYỆT yêu cầu (chỉ được duyệt yêu cầu của chi nhánh mình).
     */
    @PostMapping("/{id}/approve")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> approveRequest(@PathVariable Long id) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            Employee employee = getEmployeeFromUser(userDetails.getId());
            RegistrationRequestDto result = service.approveRequest(id, employee.getId());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    /**
     * Nhân viên TỪ CHỐI yêu cầu.
     * Body: { "reason": "..." }
     */
    @PostMapping("/{id}/reject")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> rejectRequest(@PathVariable Long id, @RequestBody Map<String, String> body) {
        try {
            UserDetailsImpl userDetails = getCurrentUser();
            Employee employee = getEmployeeFromUser(userDetails.getId());
            String reason = body.getOrDefault("reason", "Không đáp ứng yêu cầu");
            RegistrationRequestDto result = service.rejectRequest(id, employee.getId(), reason);
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new MessageResponse(e.getMessage()));
        }
    }

    // ===================== Helpers =====================

    private UserDetailsImpl getCurrentUser() {
        return (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    private Employee getEmployeeFromUser(Long userId) {
        return employeeRepo.findById(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin nhân viên!"));
    }
}
