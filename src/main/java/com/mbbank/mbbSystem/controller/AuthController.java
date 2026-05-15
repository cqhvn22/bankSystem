package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.dto.JwtResponse;
import com.mbbank.mbbSystem.dto.LoginRequest;
import com.mbbank.mbbSystem.dto.MessageResponse;
import com.mbbank.mbbSystem.dto.SignupRequest;
import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Branch;
import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.model.Employee;
import com.mbbank.mbbSystem.model.User;
import com.mbbank.mbbSystem.repository.AccountRepository;
import com.mbbank.mbbSystem.repository.BranchRepository;
import com.mbbank.mbbSystem.repository.EmployeeRepository;
import com.mbbank.mbbSystem.repository.UserRepository;
import com.mbbank.mbbSystem.security.JwtUtils;
import com.mbbank.mbbSystem.security.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Random;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BranchRepository branchRepository;

    @Autowired
    EmployeeRepository employeeRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getFullName(),
                userDetails.getEmail(),
                userDetails.getAuthorities().iterator().next().getAuthority()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        // Create new user's account based on role
        User user;
        if ("ROLE_CUSTOMER".equals(signUpRequest.getRole())) {
            com.mbbank.mbbSystem.model.Customer customer = new com.mbbank.mbbSystem.model.Customer();
            customer.setUsername(signUpRequest.getUsername());
            customer.setPassword(encoder.encode(signUpRequest.getPassword()));
            customer.setFullName(signUpRequest.getFullName());
            customer.setEmail(signUpRequest.getEmail());
            // Gán maKH: dùng giá trị từ request hoặc tự sinh nếu không có
            customer.setMaKH(signUpRequest.getMaKH() != null ? signUpRequest.getMaKH() : generateMaKH());
            if (signUpRequest.getNgaySinh() != null) customer.setNgaySinh(signUpRequest.getNgaySinh());
            if (signUpRequest.getCccd() != null) customer.setCccd(signUpRequest.getCccd());
            if (signUpRequest.getPhone() != null) customer.setPhone(signUpRequest.getPhone());
            if (signUpRequest.getAddress() != null) customer.setAddress(signUpRequest.getAddress());
            userRepository.save(customer);

            // Auto create an account (THANH_TOAN) for CUSTOMER
            String accNumber = generateAccountNumber();
            Account account = new Account(accNumber, new BigDecimal("1000000"), "THANH_TOAN", LocalDate.now(), customer);
            accountRepository.save(account);
            user = customer;
        } else {
            com.mbbank.mbbSystem.model.Employee employee = new com.mbbank.mbbSystem.model.Employee();
            employee.setUsername(signUpRequest.getUsername());
            employee.setPassword(encoder.encode(signUpRequest.getPassword()));
            employee.setFullName(signUpRequest.getFullName());
            employee.setEmail(signUpRequest.getEmail());
            employee.setRole(signUpRequest.getRole() != null ? signUpRequest.getRole() : "ROLE_EMPLOYEE");
            // Gán maNV: dùng giá trị từ request hoặc tự sinh nếu không có
            employee.setMaNV(signUpRequest.getMaNV() != null ? signUpRequest.getMaNV() : generateMaNV());
            if (signUpRequest.getBoPhan() != null) employee.setBoPhan(signUpRequest.getBoPhan());
            if (signUpRequest.getLuong() != null) employee.setLuong(new BigDecimal(signUpRequest.getLuong().toString()));
            userRepository.save(employee);
            user = employee;
        }

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }

    /**
     * Tạo khách hàng mới bởi Employee / SysAdmin (có JWT, không tự đăng ký).
     * Password được encode, tự sinh tài khoản ngân hàng.
     */
    @PostMapping("/register-customer")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('SYSADMIN')")
    public ResponseEntity<?> registerCustomerByStaff(@RequestBody SignupRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu username"));
        if (req.getPassword() == null || req.getPassword().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu password"));
        if (userRepository.existsByUsername(req.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));

        Customer customer = new Customer();
        customer.setUsername(req.getUsername());
        customer.setPassword(encoder.encode(req.getPassword()));
        customer.setFullName(req.getFullName());
        customer.setEmail(req.getEmail());
        customer.setMaKH(req.getMaKH() != null ? req.getMaKH() : generateMaKH());
        if (req.getNgaySinh() != null) customer.setNgaySinh(req.getNgaySinh());
        if (req.getCccd() != null) customer.setCccd(req.getCccd());
        if (req.getPhone() != null) customer.setPhone(req.getPhone());
        if (req.getAddress() != null) customer.setAddress(req.getAddress());
        userRepository.save(customer);

        // Auto tạo tài khoản ngân hàng
        String accNumber = "MB" + String.format("%010d", new Random().nextLong() & 0xFFFFFFFFL).substring(0, 10);
        Account account = new Account(accNumber, BigDecimal.ZERO, "THANH_TOAN", LocalDate.now(), customer);
        accountRepository.save(account);

        return ResponseEntity.ok(new MessageResponse("Tạo khách hàng thành công! Số TK: " + accNumber));
    }

    /**
     * Tạo nhân viên mới bởi SysAdmin.
     */
    @PostMapping("/register-employee")
    @PreAuthorize("hasRole('SYSADMIN')")
    public ResponseEntity<?> registerEmployeeBySysAdmin(@RequestBody SignupRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu username"));
        if (req.getPassword() == null || req.getPassword().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu password"));
        if (userRepository.existsByUsername(req.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));

        Employee employee = new Employee();
        employee.setUsername(req.getUsername());
        employee.setPassword(encoder.encode(req.getPassword()));
        employee.setFullName(req.getFullName());
        employee.setEmail(req.getEmail());
        employee.setRole(req.getRole() != null ? req.getRole() : "ROLE_EMPLOYEE");
        employee.setMaNV(req.getMaNV() != null ? req.getMaNV() : generateMaNV());
        if (req.getBoPhan() != null) employee.setBoPhan(req.getBoPhan());
        if (req.getLuong() != null) employee.setLuong(new BigDecimal(req.getLuong().toString()));
        userRepository.save(employee);

        return ResponseEntity.ok(new MessageResponse("Tạo nhân viên thành công!"));
    }

    /**
     * Tạo nhân viên mới trong chi nhánh bởi Quản lý chi nhánh.
     * Tự động gán vào chi nhánh của người tạo.
     */
    @PostMapping("/register-employee-branch")
    @PreAuthorize("hasRole('BRANCH_MANAGER')")
    public ResponseEntity<?> registerEmployeeByBranchManager(@RequestBody SignupRequest req) {
        if (req.getUsername() == null || req.getUsername().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu username"));
        if (req.getPassword() == null || req.getPassword().isBlank())
            return ResponseEntity.badRequest().body(new MessageResponse("Thiếu password"));
        if (userRepository.existsByUsername(req.getUsername()))
            return ResponseEntity.badRequest().body(new MessageResponse("Username đã tồn tại!"));

        // Lấy chi nhánh của branch manager hiện tại
        UserDetailsImpl me = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Employee manager = employeeRepository.findById(me.getId())
                .orElseThrow(() -> new RuntimeException("Không tìm thấy quản lý"));
        Branch myBranch = manager.getBranch();
        if (myBranch == null)
            return ResponseEntity.badRequest().body(new MessageResponse("Bạn chưa được phân công chi nhánh"));

        // Role chỉ được là ROLE_EMPLOYEE (Quản lý không thể tạo quản lý khác)
        String role = "ROLE_EMPLOYEE";

        Employee employee = new Employee();
        employee.setUsername(req.getUsername());
        employee.setPassword(encoder.encode(req.getPassword()));
        employee.setFullName(req.getFullName());
        employee.setEmail(req.getEmail());
        employee.setRole(role);
        employee.setMaNV(req.getMaNV() != null ? req.getMaNV() : generateMaNV());
        if (req.getBoPhan() != null) employee.setBoPhan(req.getBoPhan());
        if (req.getPosition() != null) employee.setPosition(req.getPosition());
        if (req.getLuong() != null) employee.setLuong(new BigDecimal(req.getLuong().toString()));
        employee.setBranch(myBranch);
        userRepository.save(employee);

        return ResponseEntity.ok(new MessageResponse("Tạo nhân viên thành công tại chi nhánh " + myBranch.getBranchName() + "!"));
    }

    private String generateAccountNumber() {
        Random rnd = new Random();
        int number = rnd.nextInt(999999);
        return String.format("%06d", number);
    }

    private String generateMaKH() {
        return "KH" + String.format("%06d", new Random().nextInt(999999));
    }

    private String generateMaNV() {
        return "NV" + String.format("%04d", new Random().nextInt(9999));
    }
}
