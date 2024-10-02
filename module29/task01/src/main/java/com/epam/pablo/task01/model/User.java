package com.epam.pablo.task01.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.math.BigDecimal;

import static jakarta.persistence.CascadeType.ALL;

@Entity
@Table(name = "users")
@XmlRootElement
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String email;

    @OneToOne(mappedBy = "user", cascade = ALL, orphanRemoval = true)
    private final UserAccount userAccount;

    public User() {
        userAccount = new UserAccount();
        userAccount.setUser(this);
    }

    @XmlAttribute
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

}
