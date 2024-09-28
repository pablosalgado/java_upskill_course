package com.epam.pablo.task01.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImportDataService {

    void preloadTickets();

    void preloadTickets(MultipartFile file);

}
