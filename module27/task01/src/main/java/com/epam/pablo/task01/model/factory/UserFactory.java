package com.epam.pablo.task01.model.factory;

import com.epam.pablo.task01.model.User;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    public User createUser(String name, String email) {
        var user = new User();
        user.setName(name);
        user.setEmail(email);
        return user;
    }

    public User createUser(String name, String email, BigDecimal accountBalance) {
        var user = new User();
        user.setName(name);
        user.setEmail(email);
        user.addFundsToAccount(accountBalance);
        return user;
    }

}
