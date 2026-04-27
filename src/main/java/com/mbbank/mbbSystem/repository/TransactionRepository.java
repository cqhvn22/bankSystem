package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByFromAccount_AccountNumberOrToAccount_AccountNumberOrderByTimestampDesc(String fromAccount, String toAccount);
    List<Transaction> findAllByOrderByTimestampDesc();
    Optional<Transaction> findByMaGD(String maGD);
}
