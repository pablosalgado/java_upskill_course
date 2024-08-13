package com.epam.pablo.dto;

public class DebitBankCard extends BankCard {
    private double dailyWithdrawalLimit;

    // Default constructor
    public DebitBankCard() {
    }

    // Parameterized constructor
    public DebitBankCard(String number, User user, double dailyWithdrawalLimit) {
        super(number, user);  // Call to the superclass (BankCard) constructor
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    // Getter and setter for dailyWithdrawalLimit
    public double getDailyWithdrawalLimit() {
        return dailyWithdrawalLimit;
    }

    public void setDailyWithdrawalLimit(double dailyWithdrawalLimit) {
        this.dailyWithdrawalLimit = dailyWithdrawalLimit;
    }

    // Override toString method
    @Override
    public String toString() {
        return "DebitBankCard{" +
                "number='" + getNumber() + '\'' +
                ", user=" + getUser() +
                ", dailyWithdrawalLimit=" + dailyWithdrawalLimit +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (!super.equals(o)) return false;
        if (getClass() != o.getClass()) return false;
        DebitBankCard that = (DebitBankCard) o;
        return Double.compare(that.dailyWithdrawalLimit, dailyWithdrawalLimit) == 0;
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return java.util.Objects.hash(super.hashCode(), dailyWithdrawalLimit);
    }
}