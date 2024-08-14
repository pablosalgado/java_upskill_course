package com.epam.pablo.cloud.service.impl;

import com.epam.pablo.dto.BankCard;
import com.epam.pablo.dto.Subscription;
import com.epam.pablo.dto.User;
import com.epam.pablo.service.api.Service;
import com.epam.pablo.service.api.SubscriptionNotFoundException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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
    public Optional<Subscription> getSubscriptionByBankCardNumber(String cardNumber) throws SubscriptionNotFoundException {
        return Optional.ofNullable(subscriptions.stream()
                .filter(subscription -> subscription.getBankcardNumber().equals(cardNumber))
                .findFirst()
                .orElseThrow(() -> new SubscriptionNotFoundException("Subscription not found for card number: " + cardNumber)));
    }

    @Override
    public List<User> getAllUsers() {
        return users.values().stream().collect(Collectors.toUnmodifiableList());
    }

    @Override
    public double getAverageUsersAge() {
        List<User> users = getAllUsers();
        if (users.isEmpty()) {
            return 0.0;
        }

        long totalAge = users.stream()
                .mapToLong(User::getAge)
                .sum();

        return (double) totalAge / users.size();
    }

    @Override
    public boolean isPayableUser(User user) {
        return ChronoUnit.YEARS.between(user.getBirthday(), LocalDate.now()) >= 18;
    }

    @Override
    public List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate) {
        return subscriptions.stream()
                .filter(predicate)
                .collect(Collectors.toUnmodifiableList());
    }

}
