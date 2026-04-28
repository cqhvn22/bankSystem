package com.mbbank.mbbSystem.repository;

import com.mbbank.mbbSystem.model.SavedRecipient;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface SavedRecipientRepository extends JpaRepository<SavedRecipient, Long> {
    List<SavedRecipient> findByCustomerId(Long customerId);
    Optional<SavedRecipient> findByCustomerIdAndAccountNumber(Long customerId, String accountNumber);
}
