package com.epam.pablo.cloud.bank.impl;

import com.epam.pablo.bank.api.Bank;
import com.epam.pablo.dto.*;

public class BankServiceImpl implements Bank {
    @Override
    public BankCard createBankCard(User user, BankCardType type) {
        if(type == BankCardType.CREDIT) {
            return new CreditBankCard();
        } else {
            return new DebitBankCard();
        }
    }
}
