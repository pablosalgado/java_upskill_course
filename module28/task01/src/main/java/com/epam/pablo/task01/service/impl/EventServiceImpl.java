package com.epam.pablo.task01.service.impl;

import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.service.EventService;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    @Autowired
    private EventRepository eventRepository;

    @Override
    @Transactional(readOnly = true)
    public Event getEventById(long eventId) {
        logger.debug("Fetching event by ID: {}", eventId);
        return eventRepository.findById(eventId).orElse(null);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        logger.debug("Fetching events by title: {}, page size: {}, page number: {}", title, pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return eventRepository.findByTitleContaining(title, pageable).getContent();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        logger.debug("Fetching events for day: {}, page size: {}, page number: {}", day, pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return eventRepository.findByDate(day, pageable).getContent();
    }

    @Override
    @Transactional
    public Event createEvent(Event event) {
        logger.debug("Creating new event: {}", event);
        var savedEvent = eventRepository.save(event);
        logger.debug("Event created successfully with ID: {}", savedEvent.getId());
        return savedEvent;
    }

    @Override
    @Transactional
    public Event updateEvent(Event event) {
        if (event != null && event.getId() != null) {
            logger.debug("Updating event: {}", event);
            if (eventRepository.existsById(event.getId())) {
                return eventRepository.save(event);
            } else {
                logger.warn("Attempted to update non-existing event with ID: {}", event.getId());
            }
        }
        return null;
    }

    @Override
    @Transactional
    public boolean deleteEvent(long eventId) {
        logger.debug("Deleting event with ID: {}", eventId);
        if (eventRepository.existsById(eventId)) {
            eventRepository.deleteById(eventId);
            logger.debug("Deleted event with ID: {}", eventId);
            return true;
        }
        logger.warn("Attempted to delete non-existing event with ID: {}", eventId);
        return false;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Event> getAllEvents(int pageSize, int pageNum) {
        logger.debug("Fetching all events, page size: {}, page number: {}", pageSize, pageNum);
        Pageable pageable = PageRequest.of(pageNum, pageSize);
        return eventRepository.findAll(pageable);
    }
}