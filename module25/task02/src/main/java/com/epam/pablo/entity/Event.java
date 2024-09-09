package com.epam.pablo.entity;

import java.util.Date;

/**
 * Created by maksym_govorischev.
 */
public interface Event extends Entity {
    String getTitle();
    void setTitle(String title);
    Date getDate();
    void setDate(Date date);
}
