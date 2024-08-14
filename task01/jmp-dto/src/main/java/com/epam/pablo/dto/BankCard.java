package com.epam.pablo.dto;

import java.util.Objects;

public class BankCard {
    private final String number;
    private final User user;

    public BankCard() {
        this.number = null;
        this.user = null;
    }

    public BankCard(String number, User user) {
        this.number = number;
        this.user = user;
    }

    public String getNumber() {
        return number;
    }

    public User getUser() {
        return user;
    }

    @Override
    public String toString() {
        return "BankCard{" +
                "number='" + number + '\'' +
                ", user=" + user +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var bankCard = (BankCard) o;
        return Objects.equals(number, bankCard.number) &&
                Objects.equals(user, bankCard.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(number, user);
    }
}