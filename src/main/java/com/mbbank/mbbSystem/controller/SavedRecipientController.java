package com.mbbank.mbbSystem.controller;

import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.model.SavedRecipient;
import com.mbbank.mbbSystem.repository.CustomerRepository;
import com.mbbank.mbbSystem.service.SavedRecipientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class SavedRecipientController {
    @Autowired
    private SavedRecipientService service;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer getCurrentCustomer() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        return customerRepository.findByUsername(username).orElse(null);
    }

    @GetMapping("/list")
    public ResponseEntity<List<SavedRecipient>> getContacts() {
        Customer customer = getCurrentCustomer();
        if (customer == null) return ResponseEntity.status(401).build();
        return ResponseEntity.ok(service.getSavedRecipients(customer.getId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteContact(@PathVariable Long id) {
        service.deleteRecipient(id);
        return ResponseEntity.ok().build();
    }
}
