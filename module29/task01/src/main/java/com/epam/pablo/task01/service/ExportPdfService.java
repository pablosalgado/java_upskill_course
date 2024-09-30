package com.epam.pablo.task01.service;

import java.io.ByteArrayInputStream;

public interface ExportPdfService {

    ByteArrayInputStream generateTicketsPdf();

    ByteArrayInputStream generateUserTicketsPdf(Long userId);

}