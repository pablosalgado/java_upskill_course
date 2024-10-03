package com.epam.pablo.task01.listener;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class TicketMessageListeners {

    private static final Logger logger = LoggerFactory.getLogger(TicketMessageListeners.class);

    private final BookingFacade bookingFacade;
    private final ObjectMapper objectMapper;

    public TicketMessageListeners(BookingFacade bookingFacade, ObjectMapper objectMapper) {
        this.bookingFacade = bookingFacade;
        this.objectMapper = objectMapper;
    }

    @JmsListener(destination = "createTicketQueue")
    public void onCreateTicket(Object[] message) {
        logger.debug("Received message for createTicketQueue: " + message);
        try {
            Ticket ticket = objectMapper.readValue(message[0].toString(), Ticket.class);
            Long userId = objectMapper.readValue(message[1].toString(), Long.class);
            bookingFacade.bookTicket(userId, ticket.getEvent().getId(), ticket.getPlace(), ticket.getCategory());
            logger.info("Successfully processed createTicketQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

    @JmsListener(destination = "deleteTicketQueue")
    public void onDeleteEvent(String message) {
        logger.debug("Received message for deleteTicketQueue: " + message);
        try {
            Long ticketId = objectMapper.readValue(message, Long.class);
            bookingFacade.cancelTicket(ticketId);
            logger.info("Successfully processed deleteEventQueue message: " + message);
        } catch (Exception e) {
            logger.error("Failed to process message: " + message, e);
        }
    }

}
