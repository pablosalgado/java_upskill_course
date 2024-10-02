package com.epam.pablo.task01.model;

import com.epam.pablo.task01.utils.LocalDateTimeAdapter;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    private Long id;

    @Column(unique = true, nullable = false)
    private String title;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    @Column(nullable = false)
    private LocalDateTime date;

    private BigDecimal ticketPrice = BigDecimal.ZERO;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public BigDecimal getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(BigDecimal price) {
        this.ticketPrice = price;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", title=" + title + ", date=" + date + ", price=" + ticketPrice + "]";
    }

}
