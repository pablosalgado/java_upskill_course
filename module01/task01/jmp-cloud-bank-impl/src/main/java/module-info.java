module jmp.cloud.bank.impl {
    exports com.epam.pablo.cloud.bank.impl;
    requires jmp.bank.api;
    requires jmp.dto;
    provides com.epam.pablo.bank.api.Bank with
            com.epam.pablo.cloud.bank.impl.CentralBank,
            com.epam.pablo.cloud.bank.impl.InvestmentBank,
            com.epam.pablo.cloud.bank.impl.RetailBank;
}