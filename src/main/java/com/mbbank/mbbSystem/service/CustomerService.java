package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedInfo) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setFullName(updatedInfo.getFullName());
        customer.setPhone(updatedInfo.getPhone());
        customer.setAddress(updatedInfo.getAddress());
        customer.setCccd(updatedInfo.getCccd());
        if (updatedInfo.getMaKH() != null) customer.setMaKH(updatedInfo.getMaKH());
        if (updatedInfo.getNgaySinh() != null) customer.setNgaySinh(updatedInfo.getNgaySinh());
        if (updatedInfo.getEmail() != null) customer.setEmail(updatedInfo.getEmail());
        return customerRepository.save(customer);
    }

    public void lockCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setStatus("LOCKED");
        customerRepository.save(customer);
    }

    public void unlockCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setStatus("ACTIVE");
        customerRepository.save(customer);
    }

    public Optional<Customer> getCustomer(Long id) {
        return customerRepository.findById(id);
    }

    public Optional<Customer> getCustomerByMaKH(String maKH) {
        return customerRepository.findByMaKH(maKH);
    }

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }
}
