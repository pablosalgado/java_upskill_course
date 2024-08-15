package com.epam.pablo.cloud.bank.impl;

public class RetailBank extends AbstractBank {

    public RetailBank() {
    }

    @Override
    protected String getBankPrefix() {
        return "3333";
    }
}
