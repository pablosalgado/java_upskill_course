package com.epam.pablo.cloud.bank.impl;

import com.epam.pablo.bank.api.Bank;
import com.epam.pablo.dto.*;

import java.util.Map;
import java.util.function.BiFunction;

public abstract class AbstractBank implements Bank {

    private final Map<BankCardType, BiFunction<String, User, BankCard>> cardFactory;

    protected AbstractBank() {
        cardFactory = Map.of(
                BankCardType.CREDIT, CreditBankCard::new,
                BankCardType.DEBIT, DebitBankCard::new
        );
    }

    protected abstract String getBankPrefix();

    protected String generateCardNumber() {
        var prefix = getBankPrefix();
        var suffix = Long.toString((long) (Math.random() * 1_0000_0000_0000L));
        return prefix + suffix;
    }

    @Override
    public BankCard createBankCard(User user, BankCardType type) {
        var cardNumber = generateCardNumber();
        return cardFactory.getOrDefault(type,
                        (number, usr) -> { throw new IllegalArgumentException("Unsupported card type: " + type); })
                .apply(cardNumber, user);
    }

/* Former implementation
    @Override
    public BankCard createBankCard(User user, BankCardType type) {
        switch (type) {
            case CREDIT:
                return new CreditBankCard(generateCardNumber(), user);
            case DEBIT:
                return new DebitBankCard(generateCardNumber(), user);
            default:
                throw new IllegalArgumentException("Unsupported card type: " + type);
        }
    }
*/
}
