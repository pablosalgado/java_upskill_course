package com.epam.pablo.cloud.bank.impl;

public class InvestmentBank extends AbstractBank {

    public InvestmentBank() {
    }

    @Override
    protected String getBankPrefix() {
        return "2222";
    }
}
