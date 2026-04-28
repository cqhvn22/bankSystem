package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    Optional<Customer> findByMaKH(String maKH);
    Optional<Customer> findByUsername(String username);
    /** Lấy danh sách khách hàng theo chi nhánh */
    List<Customer> findByBranchId(Long branchId);
    boolean existsByCccd(String cccd);
}
