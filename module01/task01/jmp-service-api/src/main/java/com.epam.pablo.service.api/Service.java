package com.epam.pablo.service.api;

import com.epam.pablo.dto.BankCard;
import com.epam.pablo.dto.Subscription;
import com.epam.pablo.dto.User;

import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface Service {
    void subscribe(BankCard card);

    Optional<Subscription> getSubscriptionByBankCardNumber(String cardNumber) throws SubscriptionNotFoundException;

    List<User> getAllUsers();

    double getAverageUsersAge();

    boolean isPayableUser(User user);

    List<Subscription> getAllSubscriptionsByCondition(Predicate<Subscription> predicate);
}
