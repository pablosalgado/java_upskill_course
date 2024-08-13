package com.epam.pablo.dto;

public class CreditBankCard extends BankCard {
    private double creditLimit;
    private double currentBalance;

    // Default constructor
    public CreditBankCard() {
    }

    // Parameterized constructor
    public CreditBankCard(String number, User user, double creditLimit, double currentBalance) {
        super(number, user);  // Call to the superclass (BankCard) constructor
        this.creditLimit = creditLimit;
        this.currentBalance = currentBalance;
    }

    // Getter and setter for creditLimit
    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    // Getter and setter for currentBalance
    public double getCurrentBalance() {
        return currentBalance;
    }

    public void setCurrentBalance(double currentBalance) {
        this.currentBalance = currentBalance;
    }

    // Override toString method
    @Override
    public String toString() {
        return "CreditBankCard{" +
                "number='" + getNumber() + '\'' +
                ", user=" + getUser() +
                ", creditLimit=" + creditLimit +
                ", currentBalance=" + currentBalance +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        CreditBankCard that = (CreditBankCard) o;
        return Double.compare(that.creditLimit, creditLimit) == 0 &&
                Double.compare(that.currentBalance, currentBalance) == 0;
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), creditLimit, currentBalance);
    }
}