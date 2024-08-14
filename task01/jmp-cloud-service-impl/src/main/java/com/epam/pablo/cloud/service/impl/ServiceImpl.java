package com.epam.pablo.cloud.service.impl;

import com.epam.pablo.dto.BankCard;
import com.epam.pablo.dto.Subscription;
import com.epam.pablo.dto.User;
import com.epam.pablo.service.api.Service;

import java.time.LocalDate;
import java.util.*;

public class ServiceImpl implements Service {
    private final List<Subscription> subscriptions = new ArrayList<>();
    private final Map<Integer, User> users = new HashMap<>();

    @Override
    public void subscribe(BankCard card) {
        users.put(card.getUser().hashCode(), card.getUser());

        var subscription = new Subscription(card.getNumber(), LocalDate.now());
        subscriptions.add(subscription);
    }

    @Override
    public Optional<Subscription> getSubscriptionByBankCardNumber(String cardNumber) {
        return subscriptions.stream()
                .filter(subscription -> subscription.getBankcardNumber().equals(cardNumber))
                .findFirst();
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
