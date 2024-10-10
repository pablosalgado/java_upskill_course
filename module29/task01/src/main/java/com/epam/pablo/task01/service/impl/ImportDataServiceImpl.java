package com.epam.pablo.task01.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.TicketListWrapper;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import com.epam.pablo.task01.service.ImportDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    private final ResourceLoader resourceLoader;
    private final Jaxb2Marshaller marshaller;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    Logger logger = LoggerFactory.getLogger(ImportDataServiceImpl.class);

    public ImportDataServiceImpl(ResourceLoader resourceLoader, Jaxb2Marshaller marshaller, TicketRepository ticketRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.resourceLoader = resourceLoader;
        this.marshaller = marshaller;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void preloadTickets() {
        Resource resource = resourceLoader.getResource("classpath:data.xml");
        try {
            preloadTickets(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load resource input stream", e);
        }
    }

    @Override
    public void preloadTickets(MultipartFile file) {
        try {
            preloadTickets(file.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException("Failed to load file input stream", e);
        }
    }

    private void preloadTickets(InputStream inputStream) {
        logger.info("Starting to preload tickets from input stream");

        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();

        TicketListWrapper wrapper = (TicketListWrapper) marshaller.unmarshal(new StreamSource(inputStream));
        List<Ticket> tickets = wrapper.getTickets();

        Map<String, User> userMap = new HashMap<>();
        Map<String, Event> eventMap = new HashMap<>();

        for (Ticket ticket : tickets) {
            User user = findOrSaveUser(ticket.getUser(), userMap);
            Event event = findOrSaveEvent(ticket.getEvent(), eventMap);

            ticket.setUser(user);
            ticket.setEvent(event);

            ticketRepository.save(ticket);
        }

        logger.info("Finished preloading tickets from input stream");
    }

    private User findOrSaveUser(User user, Map<String, User> userMap) {
        return userMap.computeIfAbsent(user.getEmail(), email -> {
            User existingUser = userRepository.findByEmail(email);
            return existingUser != null ? existingUser : userRepository.save(user);
        });
    }

    private Event findOrSaveEvent(Event event, Map<String, Event> eventMap) {
        return eventMap.computeIfAbsent(event.getTitle(), title -> {
            Event existingEvent = eventRepository.findByTitle(title);
            return existingEvent != null ? existingEvent : eventRepository.save(event);
        });
    }

}
