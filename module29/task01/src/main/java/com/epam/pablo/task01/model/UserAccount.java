package com.epam.pablo.task01.model;

import java.math.BigDecimal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;

@Entity
@Table(name = "user_accounts")
@XmlRootElement
public class UserAccount {

    private static final Logger logger = LoggerFactory.getLogger(UserAccount.class);
    private static final String NEGATIVE_AMOUNT_ERROR = "Amount should be positive. Attempted to %s: %s";
    private static final String INSUFFICIENT_FUNDS_ERROR = "Insufficient funds. Attempted to withdraw: %s, Available balance: %s";
    private static final String NEW_BALANCE_LOG = "New balance after %s: %s";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private BigDecimal balance = BigDecimal.ZERO;

    @XmlAttribute
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "UserAccount [id=" + id + ", user=" + user + ", balance=" + balance + "]";
    }

    public void addFunds(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            logger.error(String.format(NEGATIVE_AMOUNT_ERROR, "add", amount));
            throw new IllegalArgumentException(String.format(NEGATIVE_AMOUNT_ERROR, "add", amount));
        }
        balance = balance.add(amount);
        logger.debug(String.format(NEW_BALANCE_LOG, "adding funds", balance));
    }

    public void withdrawFunds(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) < 0) {
            logger.error(String.format(NEGATIVE_AMOUNT_ERROR, "withdraw", amount));
            throw new IllegalArgumentException(String.format(NEGATIVE_AMOUNT_ERROR, "withdraw", amount));
        }
        if (balance.compareTo(amount) < 0) {
            logger.warn(String.format(INSUFFICIENT_FUNDS_ERROR, amount, balance));
            throw new IllegalStateException(String.format(INSUFFICIENT_FUNDS_ERROR, amount, balance));
        }
        balance = balance.subtract(amount);
        logger.debug(String.format(NEW_BALANCE_LOG, "withdrawal", balance));
    }

}
