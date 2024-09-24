package com.epam.pablo.task01.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Column(unique = true)
    private String email;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private final UserAccount userAccount;

    @OneToMany(mappedBy = "user")
    private List<Ticket> tickets;

    public User() {
        userAccount = new UserAccount();
        userAccount.setUser(this);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User [id=" + id + ", name=" + name + ", email=" + email + "]";
    }

    public void addFundsToAccount(BigDecimal amount) {
        userAccount.addFunds(amount);
    }

    public void withdrawFundsFromAccount(BigDecimal amount) {
        userAccount.withdrawFunds(amount);
    }

    public BigDecimal getAccountBalance() {
        return userAccount.getBalance();
    }

    public List<Ticket> getTickets() {
        return tickets;
    }

}
