package com.epam.pablo.task01.exception;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String username) {
        super("User "+ username + " does not have enough funds to purchase the ticket.");
    }

}
