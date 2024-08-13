package com.epam.pablo.dto;

import java.time.LocalDate;

public class Subscription {
    private String bankcardNumber;
    private LocalDate startDate;

    // Default constructor
    public Subscription() {
    }

    // Parameterized constructor
    public Subscription(String bankcardNumber, LocalDate startDate) {
        this.bankcardNumber = bankcardNumber;
        this.startDate = startDate;
    }

    // Getter and setter for bankcardNumber
    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    // Getter and setter for startDate
    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    // Override toString method
    @Override
    public String toString() {
        return "Subscription{" +
                "bankcardNumber='" + bankcardNumber + '\'' +
                ", startDate=" + startDate +
                '}';
    }

    // Override equals method
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subscription that = (Subscription) o;
        return bankcardNumber.equals(that.bankcardNumber) &&
                startDate.equals(that.startDate);
    }

    // Override hashCode method
    @Override
    public int hashCode() {
        return java.util.Objects.hash(bankcardNumber, startDate);
    }
}