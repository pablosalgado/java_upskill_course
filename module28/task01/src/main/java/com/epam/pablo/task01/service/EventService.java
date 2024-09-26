package com.epam.pablo.task01.service;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;

import com.epam.pablo.task01.model.Event;

public interface EventService {

    Event getEventById(long eventId);

    List<Event> getEventsByTitle(String title, int pageSize, int pageNum);

    List<Event> getEventsForDay(Date day, int pageSize, int pageNum);

    Event createEvent(Event event);

    Event updateEvent(Event event);

    Event updateEvent(Long id, Event event);

    boolean deleteEvent(long eventId);

    Page<Event> getAllEvents(int pageSize, int pageNum);

}
