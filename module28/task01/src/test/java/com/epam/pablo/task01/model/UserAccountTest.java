package com.epam.pablo.task01.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;

class UserAccountTest {

    private UserAccount userAccount;

    @BeforeEach
    void setUp() {
        userAccount = new UserAccount();
        userAccount.setBalance(BigDecimal.ZERO);
    }

    @Test
    void testAddFunds() {
        BigDecimal amountToAdd = BigDecimal.valueOf(100.00);
        userAccount.addFunds(amountToAdd);
        Assertions.assertEquals(BigDecimal.valueOf(100.00), userAccount.getBalance(), "Balance should be 100.00 after adding funds.");
    }

    @Test
    void testAddNegativeFunds() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-50.00);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userAccount.addFunds(negativeAmount),
                "Should throw IllegalArgumentException for negative amount.");
    }

    @Test
    void testWithdrawFunds() {
        userAccount.setBalance(BigDecimal.valueOf(200.00));
        BigDecimal amountToWithdraw = BigDecimal.valueOf(150.00);
        userAccount.withdrawFunds(amountToWithdraw);
        Assertions.assertEquals(BigDecimal.valueOf(50.00), userAccount.getBalance(), "Balance should be 50.00 after withdrawal.");
    }

    @Test
    void testWithdrawInsufficientFunds() {
        userAccount.setBalance(BigDecimal.valueOf(100.00));
        BigDecimal amountToWithdraw = BigDecimal.valueOf(150.00);
        Assertions.assertThrows(IllegalStateException.class, () -> userAccount.withdrawFunds(amountToWithdraw),
                "Should throw IllegalStateException for insufficient funds.");
    }

    @Test
    void testWithdrawNegativeFunds() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-100.00);
        Assertions.assertThrows(IllegalArgumentException.class, () -> userAccount.withdrawFunds(negativeAmount),
                "Should throw IllegalArgumentException for negative amount.");
    }
}