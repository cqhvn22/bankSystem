package com.mbbank.mbbSystem.model;

import jakarta.persistence.*;

@Entity
@Table(name = "saved_recipients")
public class SavedRecipient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(nullable = false)
    private String accountNumber;

    @Column(nullable = false)
    private String fullName;

    private String nickname;

    public SavedRecipient() {}

    public SavedRecipient(Customer customer, String accountNumber, String fullName, String nickname) {
        this.customer = customer;
        this.accountNumber = accountNumber;
        this.fullName = fullName;
        this.nickname = nickname;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }
    public String getAccountNumber() { return accountNumber; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getNickname() { return nickname; }
    public void setNickname(String nickname) { this.nickname = nickname; }
}
