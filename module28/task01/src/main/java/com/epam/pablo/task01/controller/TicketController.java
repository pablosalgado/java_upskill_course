package com.epam.pablo.task01.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.repository.EventRepository;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    private final EventRepository eventService;

    public TicketController(BookingFacade bookingFacade, EventRepository eventRepository) {
        this.bookingFacade = bookingFacade;
        this.eventService = eventRepository;
    }

    @GetMapping("/new/{userId}")
    public String newTicket(Model model, @PathVariable Long userId) {
        model.addAttribute("ticket", new Ticket());
        model.addAttribute("user", bookingFacade.getUserById(userId));
        model.addAttribute("events", eventService.findAll());
        return "tickets/new";
    }

    @PostMapping
    public String createTicket(@ModelAttribute Ticket ticket, @RequestParam("userId") Long userId) {
        bookingFacade.bookTicket(userId, ticket.getEvent().getId(), ticket.getPlace(), ticket.getCategory());
        return "redirect:/users";
    }
}
