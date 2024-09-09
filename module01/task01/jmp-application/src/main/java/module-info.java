module jmp.application {
    requires jmp.cloud.bank.impl;
    requires jmp.cloud.service.impl;
    requires jmp.service.api;
    requires jmp.bank.api;
    uses com.epam.pablo.service.api.Service;
    uses com.epam.pablo.bank.api.Bank;
}