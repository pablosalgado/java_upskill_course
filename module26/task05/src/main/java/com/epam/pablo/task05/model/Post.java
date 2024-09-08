package com.epam.pablo.task05.model;

import java.sql.Timestamp;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class Post {

    private Integer id;
    private Integer userId;
    private String text;
    private java.sql.Timestamp timestamp;

    public Post(Integer id, Integer userId, String text, Timestamp time) {
        this.id = id;
        this.userId = userId;
        this.text = text;
        this.timestamp = time;
    }

    public Post() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public java.sql.Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(java.sql.Timestamp time) {
        this.timestamp = time;
    }

    @Override
    public String toString() {
        return "Post [id=" + id + ", userId=" + userId + ", texString=" + text + ", timestamp=" + timestamp + "]";
    }

}
