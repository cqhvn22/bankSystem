package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.model.SavedRecipient;
import com.mbbank.mbbSystem.repository.SavedRecipientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SavedRecipientService {
    @Autowired
    private SavedRecipientRepository repository;

    public List<SavedRecipient> getSavedRecipients(Long customerId) {
        return repository.findByCustomerId(customerId);
    }

    public void saveRecipient(Customer customer, String accountNumber, String fullName) {
        if (repository.findByCustomerIdAndAccountNumber(customer.getId(), accountNumber).isEmpty()) {
            SavedRecipient saved = new SavedRecipient(customer, accountNumber, fullName, null);
            repository.save(saved);
        }
    }

    public void deleteRecipient(Long id) {
        repository.deleteById(id);
    }
}
