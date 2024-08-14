package com.epam.pablo.application;

import com.epam.pablo.cloud.bank.impl.CentralBank;
import com.epam.pablo.cloud.bank.impl.InvestmentBank;
import com.epam.pablo.cloud.bank.impl.RetailBank;
import com.epam.pablo.dto.*;
import com.epam.pablo.cloud.service.impl.ServiceImpl;

import java.time.LocalDate;

public class Main {
    public static void main(String[] args) {
        var centralBank = new CentralBank();
        var investmentBank = new InvestmentBank();
        var retailBank = new RetailBank();

        var userJohnDoe = new User("John", "Doe", LocalDate.of(1990, 1, 1));
        var userJohnDoeCard = centralBank.createBankCard(userJohnDoe, BankCardType.CREDIT);

        var userJaneDoe = new User("Jane", "Doe", LocalDate.of(1995, 1, 1));
        var userJaneDoeCard1 = investmentBank.createBankCard(userJaneDoe, BankCardType.DEBIT);
        var userJaneDoeCard2 = centralBank.createBankCard(userJaneDoe, BankCardType.CREDIT);

        var userAliceSmith = new User("Alice", "Smith", LocalDate.of(2000, 1, 1));
        var userAliceSmithCard = retailBank.createBankCard(userAliceSmith, BankCardType.CREDIT);

        var service = new ServiceImpl();

        service.subscribe(userJohnDoeCard);
        service.subscribe(userJaneDoeCard1);
        service.subscribe(userJaneDoeCard2);
        service.subscribe(userAliceSmithCard);

        service.getSubscriptionByBankCardNumber(userJohnDoeCard.getNumber())
                .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
        service.getSubscriptionByBankCardNumber(userJaneDoeCard1.getNumber())
                .ifPresent(subscription -> System.out.println("Investment Bank Card Subscription: " + subscription));
        service.getSubscriptionByBankCardNumber(userJaneDoeCard2.getNumber())
                .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
        service.getSubscriptionByBankCardNumber(userAliceSmithCard.getNumber())
                .ifPresent(subscription -> System.out.println("Retail Bank Card Subscription: " + subscription));

        System.out.println("All users:");
        service.getAllUsers().forEach(System.out::println);

        System.out.println("Demonstration complete!");
    }
}