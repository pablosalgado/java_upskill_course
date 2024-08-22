package com.epam.pablo.service.impl;

import com.epam.pablo.entity.impl.TicketImpl;
import com.epam.pablo.service.impl.exception.EventNotFoundException;
import com.epam.pablo.service.impl.exception.UserNotFoundException;
import com.epam.pablo.storage.Storage;
import com.epam.pablo.entity.Event;
import com.epam.pablo.entity.Ticket;
import com.epam.pablo.entity.User;
import com.epam.pablo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TicketServiceImpl implements TicketService {
    private final Storage storage;

    @Autowired
    public TicketServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        Optional<User> user = Optional.ofNullable((User) storage.getById(User.class, userId));
        if (!user.isPresent()) {
            throw new UserNotFoundException("User with ID " + userId + " does not exist.");
        }

        Optional<Event> event = Optional.ofNullable((Event) storage.getById(Event.class, eventId));
        if (!event.isPresent()) {
            throw new EventNotFoundException("Event with ID " + eventId + " does not exist.");
        }

        Ticket ticket = new TicketImpl();
        ticket.setUserId(userId);
        ticket.setEventId(eventId);
        ticket.setPlace(place);
        ticket.setCategory(category);
        return (Ticket) storage.save(ticket, Ticket.class);
    }

    @Override
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return storage.selectAll(Ticket.class)
                .stream()
                .filter(ticket -> ((Ticket)ticket).getUserId() == user.getId())
                .skip(pageSize * pageNum)
                .limit(pageSize)
                .toList();
    }

    @Override
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        return storage.selectAll(Ticket.class)
                .stream()
                .filter(ticket -> ((Ticket)ticket).getEventId() == event.getId())
                .skip(pageSize * pageNum)
                .limit(pageSize)
                .toList();
    }

    @Override
    public boolean cancelTicket(long ticketId) {
        return storage.delete(Ticket.class, ticketId);
    }
}
