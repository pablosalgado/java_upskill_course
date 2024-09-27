package com.epam.pablo.task01.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;

public interface TicketService {

    /**
     * Books a ticket for a specified user and event.
     *
     * @param userId   the ID of the user booking the ticket
     * @param eventId  the ID of the event for which the ticket is being booked
     * @param place    the place number for the ticket
     * @param category the category of the ticket
     * @return the booked Ticket object
     */
    Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category);

    /**
     * Retrieves a ticket by its unique identifier.
     *
     * @param id the unique identifier of the ticket
     * @return the ticket associated with the given id
     */
    Ticket getTicketById(Long id);

    /**
     * Retrieves a paginated list of booked tickets.
     *
     * @param pageSize the number of tickets to be included in a single page.
     * @param pageNum the page number to retrieve.
     * @return a Page object containing the booked tickets for the specified page.
     */
    Page<Ticket> getBookedTickets(int pageSize, int pageNum);

    /**
     * Retrieves a list of booked tickets for a specific user.
     *
     * @param user the user whose booked tickets are to be retrieved
     * @param pageSize the number of tickets to be returned per page
     * @param pageNum the page number to retrieve
     * @return a list of booked tickets for the specified user
     */
    List<Ticket> getBookedTickets(User user, int pageSize, int pageNum);

    /**
     * Retrieves a paginated list of booked tickets for a specific user.
     *
     * @param user the user whose booked tickets are to be retrieved
     * @param pageSize the number of tickets to be included in a single page
     * @param pageNum the page number to retrieve
     * @return a page of booked tickets for the specified user
     */
    Page<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum);

    /**
     * Retrieves a list of booked tickets for a specified event.
     *
     * @param event the event for which booked tickets are to be retrieved
     * @param pageSize the number of tickets to be retrieved per page
     * @param pageNum the page number to retrieve tickets from
     * @return a list of booked tickets for the specified event
     */
    List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum);

    /**
     * Cancels a ticket with the specified ticket ID.
     *
     * @param ticketId the ID of the ticket to be canceled
     * @return true if the ticket was successfully canceled, false otherwise
     */
    boolean cancelTicket(long ticketId);

}