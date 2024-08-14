package com.epam.pablo.cloud.bank.impl;

public class CentralBank extends AbstractBank {

    @Override
    protected String getBankPrefix() {
        return "1111";
    }
}
