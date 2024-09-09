module jmp.cloud.service.impl {
    exports com.epam.pablo.cloud.service.impl;
    requires jmp.service.api;
    requires jmp.dto;
    provides com.epam.pablo.service.api.Service with com.epam.pablo.cloud.service.impl.ServiceImpl;
}