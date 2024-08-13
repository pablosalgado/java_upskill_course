package com.epam.pablo.service.api;

import com.epam.pablo.dto.BankCard;
import com.epam.pablo.dto.Subscription;
import com.epam.pablo.dto.User;

import java.util.List;
import java.util.Optional;

public interface Service {
    void subscribe(BankCard card);

    Optional<Subscription> getSubscriptionByBankCardNumber(String cardNumber);

    List<User> getAllUsers();
}
