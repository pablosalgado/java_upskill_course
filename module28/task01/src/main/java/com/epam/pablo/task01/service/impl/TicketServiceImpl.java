package com.epam.pablo.task01.service.impl;

import java.util.List;

import org.hibernate.annotations.DialectOverride.OverridesAnnotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.pablo.task01.exception.EventNotFoundException;
import com.epam.pablo.task01.exception.InsufficientFundsException;
import com.epam.pablo.task01.exception.UserNotFoundException;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.model.factory.TicketFactory;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import com.epam.pablo.task01.service.TicketService;

@Service
public class TicketServiceImpl implements TicketService {

    private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private TicketFactory ticketFactory;

    @Override
    @Transactional
    public Ticket bookTicket(long userId, long eventId, int place, Ticket.Category category) {
        logger.debug("Attempting to book ticket for user ID: {}, event ID: {}, place: {}, category: {}", userId, eventId, place, category);

        var user = userRepository.findById(userId).orElseThrow(() -> {
            logger.error("User not found with ID: {}", userId);
            return new UserNotFoundException(userId);
        });

        var event = eventRepository.findById(eventId).orElseThrow(() -> {
            logger.error("Event not found with ID: {}", eventId);
            return new EventNotFoundException(eventId);
        });

        if (user.getAccountBalance().compareTo(event.getTicketPrice()) < 0) {
            logger.error("User does not have enough funds to purchase the ticket");
            throw new InsufficientFundsException(user.getName());
        }

        var ticket = ticketFactory.createTicket(user, event, place, category);
        
        user.withdrawFundsFromAccount(event.getTicketPrice());
        userRepository.save(user);

        Ticket savedTicket = ticketRepository.save(ticket);

        logger.debug("Ticket booked successfully with ID: {}", savedTicket.getId());
        return savedTicket;
    }

    @Override
    @Transactional(readOnly = true)
    public Ticket getTicketById(Long id) {
        logger.debug("Fetching ticket by ID: {}", id);
        return ticketRepository.findById(id).orElseThrow();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ticket> getBookedTickets(int pageSize, int pageNum) {
        logger.debug("Fetching booked tickets, page size: {}, page number: {}", pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ticketRepository.findAllWithUserAndEvent(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<Ticket> getBookedTickets(User user, int pageSize, int pageNum) {
        return getBookedTicketsByUser(user, pageSize, pageNum).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ticket> getBookedTicketsByUser(User user, int pageSize, int pageNum) {
        logger.debug("Fetching booked tickets for user ID: {}, page size: {}, page number: {}", user.getId(), pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ticketRepository.findByUserIdWithUserAndEvent(user.getId(), pageable);
    }

    @Override
    @Transactional(readOnly = true)
    @Deprecated
    public List<Ticket> getBookedTickets(Event event, int pageSize, int pageNum) {
        logger.debug("Fetching booked tickets for event ID: {}, page size: {}, page number: {}", event.getId(), pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return ticketRepository.findByEventId(event.getId(), pageable).getContent();
    }

    @Override
    @Transactional
    public boolean cancelTicket(long ticketId) {
        logger.debug("Attempting to cancel ticket with ID: {}", ticketId);

        var ticket = ticketRepository.findById(ticketId).orElse(null);

        if (ticket != null) {
            var user = ticket.getUser();
            user.addFundsToAccount(ticket.getEvent().getTicketPrice());
            userRepository.save(user);

            ticketRepository.deleteById(ticketId);            
            logger.debug("Ticket cancelled successfully with ID: {}", ticketId);
            return true;
        }
        logger.warn("Attempted to cancel non-existing ticket with ID: {}", ticketId);
        return false;
    }
}