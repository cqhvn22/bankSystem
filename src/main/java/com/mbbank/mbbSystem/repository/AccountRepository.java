package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    Boolean existsByAccountNumber(String accountNumber);
    List<Account> findByCustomerBranchId(Long branchId);
}
