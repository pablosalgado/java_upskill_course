package com.epam.pablo.task01.controller;

import org.springframework.data.domain.Page;
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
import com.epam.pablo.task01.model.User;
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

    @GetMapping
    public String listAllTickets(Model model,
                            @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<Ticket> ticketPage = bookingFacade.getBookedTickets(size, page);
        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());
        return "tickets/index";
    }

    @GetMapping("/user/{userId}")
    public String listTicketsByUser(Model model, @PathVariable Long userId,
                            @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        User user = bookingFacade.getUserById(userId);
        Page<Ticket> ticketPage = bookingFacade.getBookedTicketsByUser(user, size, page);
        model.addAttribute("tickets", ticketPage.getContent());
        model.addAttribute("user", user);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", ticketPage.getTotalPages());
        return "tickets/list";
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

    @GetMapping("/delete/{id}")
    public String deleteTicket(@PathVariable Long id) {
        Ticket ticket = bookingFacade.getTicketById(id); 
        bookingFacade.cancelTicket(id);
        return "redirect:/tickets/user/" + ticket.getUser().getId();
    }

}
