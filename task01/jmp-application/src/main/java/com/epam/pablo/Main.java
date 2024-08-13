package com.epam.pablo;

import com.epam.pablo.dto.CreditBankCard;
import com.epam.pablo.service.impl.ServiceImpl;

public class Main {
    public static void main(String[] args) {
        var service = new ServiceImpl();

        var bankCard = new CreditBankCard();
        service.subscribe(bankCard);

        service.getAllUsers().forEach(System.out::println);

        System.out.println("Hello world!");
    }
}