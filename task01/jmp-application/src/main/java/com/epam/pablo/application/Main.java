package com.epam.pablo.application;

import com.epam.pablo.bank.api.Bank;
import com.epam.pablo.cloud.bank.impl.CentralBank;
import com.epam.pablo.cloud.bank.impl.InvestmentBank;
import com.epam.pablo.cloud.bank.impl.RetailBank;
import com.epam.pablo.dto.*;
import com.epam.pablo.cloud.service.impl.ServiceImpl;
import com.epam.pablo.service.api.Service;
import com.epam.pablo.service.api.SubscriptionNotFoundException;

import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.List;

import java.time.LocalDate;

public class Main {
    /**
     * Main method to demonstrate the application using ServiceLoader.load.
     * @param args command line arguments
     */
    /*
    public static void main(String[] args) {
        // Load Bank implementations dynamically
        ServiceLoader<Bank> bankLoader = ServiceLoader.load(Bank.class);
        System.out.println("Loaded banks: " + bankLoader.stream().count());

        Bank centralBank = null, investmentBank = null, retailBank = null;

        for (Bank bank : bankLoader) {
            if (bank.getClass().getSimpleName().equals("CentralBank")) {
                centralBank = bank;
            } else if (bank.getClass().getSimpleName().equals("InvestmentBank")) {
                investmentBank = bank;
            } else if (bank.getClass().getSimpleName().equals("RetailBank")) {
                retailBank = bank;
            }
        }

        var userJohnDoe = new User("John", "Doe", LocalDate.of(1990, 1, 1));
        var userJohnDoeCard = centralBank.createBankCard(userJohnDoe, BankCardType.CREDIT);

        var userJaneDoe = new User("Jane", "Doe", LocalDate.of(1995, 2, 2));
        var userJaneDoeCard1 = investmentBank.createBankCard(userJaneDoe, BankCardType.DEBIT);
        var userJaneDoeCard2 = centralBank.createBankCard(userJaneDoe, BankCardType.CREDIT);

        var userAliceSmith = new User("Alice", "Smith", LocalDate.of(2000, 3, 10));
        var userAliceSmithCard = retailBank.createBankCard(userAliceSmith, BankCardType.CREDIT);

        var userBobSmith = new User("Bob", "Smith", LocalDate.of(2010, 11, 3));
        var userBobSmithCard = retailBank.createBankCard(userBobSmith, BankCardType.DEBIT);

        var service = ServiceLoader.load(Service.class).findFirst().orElseThrow();

        service.subscribe(userJohnDoeCard);
        service.subscribe(userJaneDoeCard1);
        service.subscribe(userJaneDoeCard2);
        service.subscribe(userAliceSmithCard);
        service.subscribe(userBobSmithCard);

        try {
            service.getSubscriptionByBankCardNumber(userJohnDoeCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userJaneDoeCard1.getNumber())
                    .ifPresent(subscription -> System.out.println("Investment Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userJaneDoeCard2.getNumber())
                    .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userAliceSmithCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Retail Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userBobSmithCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Retail Bank Card Subscription: " + subscription));
        } catch (SubscriptionNotFoundException ignored) {
        } finally {
            System.out.println("All subscriptions are valid.\n");
        }


        try {
            service.getSubscriptionByBankCardNumber("1234567890");
        } catch (SubscriptionNotFoundException e) {
            System.out.println("Non-existent card subscription number 1234567890 not found.\n");
        }

        System.out.println("All users:");
        service.getAllUsers().forEach(System.out::println);
        System.out.println();

        System.out.printf("Average users age: %.2f years.\n\n", service.getAverageUsersAge());

        System.out.println("Is John Doe payable user: " + service.isPayableUser(userJohnDoe));
        System.out.println("Is Jane Doe payable user: " + service.isPayableUser(userJaneDoe));
        System.out.println("Is Alice Smith payable user: " + service.isPayableUser(userAliceSmith));
        System.out.println("Is Bob Smith payable user: " + service.isPayableUser(userBobSmith));
        System.out.println();

        Predicate<Subscription> predicate = subscription ->
                subscription.getBankcardNumber().startsWith("3333");

        List<Subscription> activeSubscriptions = service.getAllSubscriptionsByCondition(predicate);

        activeSubscriptions.forEach(subscription ->
                System.out.println("Active Subscription: " + subscription.getBankcardNumber()));

        System.out.println("\nDemonstration complete!");
    }
    */

    /**
     * Main method to demonstrate the application using direct instantiation.
     * @param args
     */
    public static void main(String[] args) {
        var centralBank = new CentralBank();
        var investmentBank = new InvestmentBank();
        var retailBank = new RetailBank();

        var userJohnDoe = new User("John", "Doe", LocalDate.of(1990, 1, 1));
        var userJohnDoeCard = centralBank.createBankCard(userJohnDoe, BankCardType.CREDIT);

        var userJaneDoe = new User("Jane", "Doe", LocalDate.of(1995, 2, 2));
        var userJaneDoeCard1 = investmentBank.createBankCard(userJaneDoe, BankCardType.DEBIT);
        var userJaneDoeCard2 = centralBank.createBankCard(userJaneDoe, BankCardType.CREDIT);

        var userAliceSmith = new User("Alice", "Smith", LocalDate.of(2000, 3, 10));
        var userAliceSmithCard = retailBank.createBankCard(userAliceSmith, BankCardType.CREDIT);

        var userBobSmith = new User("Bob", "Smith", LocalDate.of(2010, 11, 3));
        var userBobSmithCard = retailBank.createBankCard(userBobSmith, BankCardType.DEBIT);

        var service = new ServiceImpl();

        service.subscribe(userJohnDoeCard);
        service.subscribe(userJaneDoeCard1);
        service.subscribe(userJaneDoeCard2);
        service.subscribe(userAliceSmithCard);
        service.subscribe(userBobSmithCard);

        try {
            service.getSubscriptionByBankCardNumber(userJohnDoeCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userJaneDoeCard1.getNumber())
                    .ifPresent(subscription -> System.out.println("Investment Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userJaneDoeCard2.getNumber())
                    .ifPresent(subscription -> System.out.println("Central Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userAliceSmithCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Retail Bank Card Subscription: " + subscription));
            service.getSubscriptionByBankCardNumber(userBobSmithCard.getNumber())
                    .ifPresent(subscription -> System.out.println("Retail Bank Card Subscription: " + subscription));
        } catch (SubscriptionNotFoundException ignored) {
        } finally {
            System.out.println("All subscriptions are valid.\n");
        }


        try {
            service.getSubscriptionByBankCardNumber("1234567890");
        } catch (SubscriptionNotFoundException e) {
            System.out.println("Non-existent card subscription number 1234567890 not found.\n");
        }

        System.out.println("All users:");
        service.getAllUsers().forEach(System.out::println);
        System.out.println();

        System.out.printf("Average users age: %.2f years.\n\n", service.getAverageUsersAge());

        System.out.println("Is John Doe payable user: " + service.isPayableUser(userJohnDoe));
        System.out.println("Is Jane Doe payable user: " + service.isPayableUser(userJaneDoe));
        System.out.println("Is Alice Smith payable user: " + service.isPayableUser(userAliceSmith));
        System.out.println("Is Bob Smith payable user: " + service.isPayableUser(userBobSmith));
        System.out.println();

        Predicate<Subscription> predicate = subscription ->
                subscription.getBankcardNumber().startsWith("3333");

        List<Subscription> activeSubscriptions = service.getAllSubscriptionsByCondition(predicate);

        activeSubscriptions.forEach(subscription ->
                System.out.println("Active Subscription: " + subscription.getBankcardNumber()));

        System.out.println("\nDemonstration complete!");
    }
}