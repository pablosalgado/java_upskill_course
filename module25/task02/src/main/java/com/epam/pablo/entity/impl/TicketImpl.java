package com.epam.pablo.entity.impl;

import com.epam.pablo.entity.Ticket;

import java.util.Objects;

public class TicketImpl implements Ticket {
    private long id;
    private long userId;
    private long eventId;
    private int place;
    private Ticket.Category category;

    @Override
    public long getId() {
        return id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getUserId() {
        return userId;
    }

    @Override
    public void setUserId(long userId) {
        this.userId = userId;
    }

    @Override
    public long getEventId() {
        return  eventId;
    }

    @Override
    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    @Override
    public int getPlace() {
        return  place;
    }

    @Override
    public void setPlace(int place) {
        this.place = place;
    }

    @Override
    public Ticket.Category getCategory() {
        return category;
    }

    @Override
    public void setCategory(Ticket.Category category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TicketImpl ticket = (TicketImpl) o;
        return id == ticket.id && userId == ticket.userId && eventId == ticket.eventId && place == ticket.place && category == ticket.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId, eventId, place, category);
    }

    @Override
    public String toString() {
        return "TicketImpl{" +
                "id=" + id +
                ", userId=" + userId +
                ", eventId=" + eventId +
                ", place=" + place +
                ", category=" + category +
                '}';
    }
}
