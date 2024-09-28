package com.epam.pablo.task01.facade.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.epam.pablo.task01.service.ImportDataService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.service.EventService;
import com.epam.pablo.task01.service.TicketService;
import com.epam.pablo.task01.service.UserService;

@Service
public class BookingFacadeImpl implements BookingFacade {

    private final UserService userService;
    private final EventService eventService;
    private final TicketService ticketService;
    private final ImportDataService importDataService;

    public BookingFacadeImpl(UserService userService, EventService eventService, TicketService ticketService, ImportDataService importService) {
        this.userService = userService;
        this.eventService = eventService;
        this.ticketService = ticketService;
        this.importDataService = importService;
    }

    @Override
    public Event getEventById(long id) {
        return eventService.getEventById(id);
    }

    @Override
    @Deprecated
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return eventService.getEventsByTitle(title, pageSize, pageNum);
    }

    @Override
    @Deprecated
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        return eventService.getEventsForDay(day, pageSize, pageNum);
    }

    @Override
    public Event createEvent(Event event) {
        return eventService.createEvent(event);
    }

    @Override
    public Event updateEvent(Long id, Event event) {
        return eventService.updateEvent(id, event);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return eventService.deleteEvent(eventId);
    }

    @Override
    public User getUserById(long id) {
        return userService.getUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return userService.getUserByEmail(email);
    }

    @Override
    @Deprecated
    public List<User> getUsersByName(String name, int pageSize, int pageNum) {
        return userService.getUsersByName(name, pageSize, pageNum);
    }

    @Override
    public User createUser(User user) {
        return userService.createUser(user);
    }

    @Override
    public User updateUser(Long id, User user) {
        return userService.updateUser(id, user);
    }

    @Override
    public boolean deleteUser(long userId) {
        return userService.deleteUser(userId);
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        return ticketService.bookTicket(userId, eventId, place, category);
    }

    @Override
    public Ticket getTicketById(Long id) {
        return ticketService.getTicketById(id);
    }

    @Override
    public Page<Ticket> getBookedTickets(int pageSize, int pageNum) {
        return ticketService.getBookedTickets(pageSize, pageNum);
    }

    @Override
    @Deprecated
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(user, pageSize, pageNum);
    }

    @Override
    public Page<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum) {
        return ticketService.getBookedTicketsByUser(user, pageSize, pageNum);
    }

    @Override
    @Deprecated
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return ticketService.getBookedTickets(event, pageSize, pageNum);
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return ticketService.cancelTicket(ticketId);
    }

    @Override
    public void refillAccount(long userId, BigDecimal amount) {
        userService.refillAccount(userId, amount);
    }

    @Override
    public Page<User> getAllUsers(int pageSize, int pageNum) {
        return userService.getAllUsers(pageSize, pageNum);
    }

    @Override
    public Page<Event> getAllEvents(int pageSize, int pageNum) {
        return eventService.getAllEvents(pageSize, pageNum);
    }

    @Override
    public void preloadTickets() {
        importDataService.preloadTickets();
    }

    @Override
    public void preloadTickets(MultipartFile file) {
        importDataService.preloadTickets(file);
    }

}
