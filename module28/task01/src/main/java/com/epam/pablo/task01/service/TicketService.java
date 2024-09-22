package com.epam.pablo.task01.service;

import java.util.List;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;

public interface TicketService {

    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    boolean cancelTicket(long ticketId);

}