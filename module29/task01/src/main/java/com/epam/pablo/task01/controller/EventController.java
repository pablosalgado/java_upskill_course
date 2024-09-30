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
import com.epam.pablo.task01.model.Event;

@Controller
@RequestMapping("/events")
public class EventController {

    private static final String DEFAULT_PAGE_SIZE = "10";
    private static final String DEFAULT_PAGE_NUM = "0";
    private final BookingFacade bookingFacade;
    
        public EventController(BookingFacade bookingFacade) {
        this.bookingFacade = bookingFacade;
    }

    @GetMapping
    public String listEvents(Model model,
                            @RequestParam(defaultValue = DEFAULT_PAGE_NUM) int page,
                            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int size) {
        Page<Event> eventPage = bookingFacade.getAllEvents(size, page);
        model.addAttribute("events", eventPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", eventPage.getTotalPages());
        return "events/list";
    }

    @GetMapping("/{id}")
    public String getEvent(Model model, @PathVariable Long id) {
        model.addAttribute("event", bookingFacade.getEventById(id));
        return "events/show";
    }

    @GetMapping("/new")
    public String newEvent(Model model) {
        model.addAttribute("event", new Event());
        return "events/new";
    }

    @PostMapping
    public String createEvent(@ModelAttribute Event event) {
        bookingFacade.createEvent(event);
        return "redirect:/events";
    }

    @GetMapping("/edit/{id}")
    public String editEvent(Model model, @PathVariable Long id) {
        model.addAttribute("event", bookingFacade.getEventById(id));
        return "events/edit";
    }

    @PostMapping("/{id}")
    public String updateEvent(@PathVariable Long id, @ModelAttribute Event event) {
        bookingFacade.updateEvent(id, event);
        return "redirect:/events";
    }

    @GetMapping("/delete/{id}")
    public String deleteEvent(@PathVariable Long id) {
        bookingFacade.deleteEvent(id);
        return "redirect:/events";
    }

}
