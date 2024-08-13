package com.epam.pablo.dto;

public class BankCard {
    private String number;
    private User user;

    // Default constructor
    public BankCard() {
    }

    // Parameterized constructor
    public BankCard(String number, User user) {
        this.number = number;
        this.user = user;
    }

    // Getter and setter for number
    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    // Getter and setter for user
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    // Override toString method
    @Override
    public String toString() {
        return "BankCard{" +
                "number='" + number + '\'' +
                ", user=" + user +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankCard bankCard = (BankCard) o;
        return number.equals(bankCard.number) &&
                user.equals(bankCard.user);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return java.util.Objects.hash(number, user);
    }
}