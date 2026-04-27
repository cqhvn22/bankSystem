package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.AccountRegistrationRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRegistrationRequestRepository extends JpaRepository<AccountRegistrationRequest, Long> {

    /** Lấy tất cả yêu cầu của một chi nhánh (lọc theo branch.id) */
    List<AccountRegistrationRequest> findByBranchId(Long branchId);

    /** Lấy yêu cầu PENDING của một chi nhánh */
    List<AccountRegistrationRequest> findByBranchIdAndStatus(Long branchId, String status);

    /** Kiểm tra CCCD đã gửi yêu cầu chưa xử lý chưa */
    Optional<AccountRegistrationRequest> findByCccdAndStatus(String cccd, String status);

    /** Kiểm tra username đã tồn tại trong requests */
    boolean existsByDesiredUsername(String desiredUsername);

    /** Kiểm tra CCCD đã tồn tại trong requests */
    boolean existsByCccd(String cccd);

    /** Lấy tất cả yêu cầu theo status */
    List<AccountRegistrationRequest> findByStatus(String status);
}
