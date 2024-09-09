package com.epam.pablo.cloud.bank.impl;

public class CentralBank extends AbstractBank {

    public CentralBank() {
    }

    @Override
    protected String getBankPrefix() {
        return "1111";
    }
}
