package com.epam.pablo.entity;

/**
 * Created by maksym_govorischev.
 */
public interface Ticket extends Entity {
    public enum Category {STANDARD, PREMIUM, BAR}

    long getEventId();
    void setEventId(long eventId);
    long getUserId();
    void setUserId(long userId);
    Category getCategory();
    void setCategory(Category category);
    int getPlace();
    void setPlace(int place);
}
