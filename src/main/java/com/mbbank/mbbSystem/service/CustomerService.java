package com.mbbank.mbbSystem.service;

import com.mbbank.mbbSystem.model.Account;
import com.mbbank.mbbSystem.model.Customer;
import com.mbbank.mbbSystem.repository.AccountRepository;
import com.mbbank.mbbSystem.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AccountRepository accountRepository;

    public Customer addCustomer(Customer customer) {
        if (customerRepository.existsByCccd(customer.getCccd())) {
            throw new RuntimeException("Số CCCD này đã tồn tại trên hệ thống!");
        }
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer updatedInfo) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setFullName(updatedInfo.getFullName());
        customer.setPhone(updatedInfo.getPhone());
        customer.setAddress(updatedInfo.getAddress());
        
        if (updatedInfo.getCccd() != null && !updatedInfo.getCccd().equals(customer.getCccd())) {
            if (customerRepository.existsByCccd(updatedInfo.getCccd())) {
                throw new RuntimeException("Số CCCD mới này đã tồn tại trên hệ thống!");
            }
            customer.setCccd(updatedInfo.getCccd());
        }
        if (updatedInfo.getMaKH() != null) customer.setMaKH(updatedInfo.getMaKH());
        if (updatedInfo.getNgaySinh() != null) customer.setNgaySinh(updatedInfo.getNgaySinh());
        if (updatedInfo.getEmail() != null) customer.setEmail(updatedInfo.getEmail());
        return customerRepository.save(customer);
    }

    public void lockCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setStatus("LOCKED");
        customerRepository.save(customer);

        // Đồng thời khóa tài khoản ngân hàng của khách hàng
        List<Account> accounts = accountRepository.findByCustomerId(id);
        for (Account acc : accounts) {
            acc.setStatus("DA_KHOA");
            accountRepository.save(acc);
        }
    }

    public void unlockCustomer(Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setStatus("ACTIVE");
        customerRepository.save(customer);

        // Đồng thời mở khóa tài khoản ngân hàng của khách hàng
        List<Account> accounts = accountRepository.findByCustomerId(id);
        for (Account acc : accounts) {
            acc.setStatus("HOAT_DONG");
            accountRepository.save(acc);
        }
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

    /** Lấy danh sách khách hàng theo chi nhánh — dùng khi nhân viên chỉ xem KH của chi nhánh mình */
    public List<Customer> getCustomersByBranch(Long branchId) {
        return customerRepository.findByBranchId(branchId);
    }

    /**
     * Lấy tài khoản duy nhất của khách hàng.
     */
    public Optional<Account> getCustomerAccount(Long customerId) {
        List<Account> accounts = accountRepository.findByCustomerId(customerId);
        return accounts.isEmpty() ? Optional.empty() : Optional.of(accounts.get(0));
    }
}
