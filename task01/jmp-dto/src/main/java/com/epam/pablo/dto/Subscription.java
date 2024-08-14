package com.epam.pablo.dto;

import java.time.LocalDate;

public class Subscription {
    private String bankcardNumber;
    private LocalDate startDate;

    public Subscription() {
    }

    public Subscription(String bankcardNumber, LocalDate startDate) {
        this.bankcardNumber = bankcardNumber;
        this.startDate = startDate;
    }

    public String getBankcardNumber() {
        return bankcardNumber;
    }

    public void setBankcardNumber(String bankcardNumber) {
        this.bankcardNumber = bankcardNumber;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Override
    public String toString() {
        return "Subscription{" +
                "bankcardNumber='" + bankcardNumber + '\'' +
                ", startDate=" + startDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        var that = (Subscription) o;
        return bankcardNumber.equals(that.bankcardNumber) &&
                startDate.equals(that.startDate);
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(bankcardNumber, startDate);
    }
}