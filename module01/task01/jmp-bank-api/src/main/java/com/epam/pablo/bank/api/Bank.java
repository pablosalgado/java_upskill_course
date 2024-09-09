package com.epam.pablo.bank.api;

import com.epam.pablo.dto.BankCard;
import com.epam.pablo.dto.BankCardType;
import com.epam.pablo.dto.User;

public interface Bank {
    BankCard createBankCard(User user, BankCardType type);
}
