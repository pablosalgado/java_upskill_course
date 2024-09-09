package com.epam.pablo.service.impl;

import com.epam.pablo.storage.Storage;
import com.epam.pablo.entity.Event;
import com.epam.pablo.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class EventServiceImpl implements EventService {
    private final Storage storage;

    @Autowired
    public EventServiceImpl(Storage storage) {
        this.storage = storage;
    }

    @Override
    public Event getEventById(long id) {
        return (Event) storage.getById(Event.class, id);
    }

    @Override
    public List<Event> getEventsByTitle(String title, int pageSize, int pageNum) {
        return storage.selectAll(Event.class)
                .stream()
                .filter(event -> ((Event)event).getTitle().equals(title))
                .skip(pageSize * pageNum)
                .limit(pageSize)
                .toList();
    }

    @Override
    public List<Event> getEventsForDay(Date day, int pageSize, int pageNum) {
        var targetDay = day.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        return storage.selectAll(Event.class)
                .stream()
                .filter(event -> {
                    var eventDay = ((Event)event).getDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                    return eventDay.equals(targetDay);
                })
                .skip(pageSize * pageNum)
                .limit(pageSize)
                .toList();
    }

    @Override
    public Event createEvent(Event event) {
        return (Event) storage.save(event, Event.class);
    }

    @Override
    public Event updateEvent(Event event) {
        return (Event) storage.update(event, Event.class);
    }

    @Override
    public boolean deleteEvent(long eventId) {
        return storage.delete(Event.class, eventId);
    }
}
