package com.epam.pablo.dto;

import java.util.Objects;

public class DebitBankCard extends BankCard {

    public DebitBankCard() {
        super();
    }

    public DebitBankCard(String number, User user) {
        super(number, user);
    }

    @Override
    public String toString() {
        return "DebitBankCard{" +
                "number='" + getNumber() + '\'' +
                ", user=" + getUser() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false;
        return getClass() == o.getClass();
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode());
    }
}