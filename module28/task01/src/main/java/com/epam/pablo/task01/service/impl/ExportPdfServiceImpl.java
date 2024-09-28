package com.epam.pablo.task01.service.impl;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;
import java.time.format.DateTimeFormatter;

import org.springframework.stereotype.Service;

import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.service.ExportPdfService;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;

@Service
public class ExportPdfServiceImpl implements ExportPdfService {

    private final TicketRepository ticketRepository;

    public ExportPdfServiceImpl(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }

    @Override
    public ByteArrayInputStream generateTicketsPdf() {
        return generatePdf(ticketRepository.findAll());
    }

    @Override
    public ByteArrayInputStream generateUserTicketsPdf(Long userId) {
        return generatePdf(ticketRepository.findByUserId(userId));
    }

    private ByteArrayInputStream generatePdf(List<Ticket> tickets) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(out);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        try {
            document.add(new Paragraph("List of Tickets")
                .setTextAlignment(TextAlignment.CENTER)
                .setBold()
                .setFontSize(14));

            float[] columnWidths = {1, 3, 2, 2};
            Table table = new Table(columnWidths);
            table.setWidth(UnitValue.createPercentValue(100));
            table.addHeaderCell("Ticket ID");
            table.addHeaderCell("Event Name");
            table.addHeaderCell("Date");
            table.addHeaderCell("Name");

            for (Ticket ticket : tickets) {
                table.addCell(ticket.getId().toString());
                table.addCell(ticket.getEvent().getTitle());
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
                table.addCell(ticket.getEvent().getDate().format(formatter));
                table.addCell(ticket.getUser().getName());
            }

            document.add(table);
        } finally {
            document.close();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

}
