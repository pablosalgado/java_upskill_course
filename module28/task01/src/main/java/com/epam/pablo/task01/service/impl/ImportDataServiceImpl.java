package com.epam.pablo.task01.service.impl;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.transform.stream.StreamSource;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.stereotype.Service;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.TicketListWrapper;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import com.epam.pablo.task01.service.ImportDataService;

@Service
public class ImportDataServiceImpl implements ImportDataService {

    private final ResourceLoader resourceLoader;
    private final Jaxb2Marshaller marshaller;
    private final EventRepository eventRepository;
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    

    public ImportDataServiceImpl(ResourceLoader resourceLoader, Jaxb2Marshaller marshaller, TicketRepository ticketRepository, EventRepository eventRepository, UserRepository userRepository) {
        this.resourceLoader = resourceLoader;
        this.marshaller = marshaller;
        this.eventRepository = eventRepository;
        this.ticketRepository = ticketRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void preloadTickets() {
        ticketRepository.deleteAll();
        eventRepository.deleteAll();
        userRepository.deleteAll();
    
        Resource resource = resourceLoader.getResource("classpath:data.xml");
    
        try (InputStream is = resource.getInputStream()) {
            TicketListWrapper wrapper = (TicketListWrapper) marshaller.unmarshal(new StreamSource(is));
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
        } catch (Exception e) {
            throw new RuntimeException("Failed to preload tickets", e);
        }
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
