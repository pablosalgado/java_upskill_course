package com.epam.pablo.task01.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.epam.pablo.task01.utils.LocalDateTimeAdapter;

import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAttribute;
import jakarta.xml.bind.annotation.XmlRootElement;
import jakarta.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

@Entity
@Table(name = "events")
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @XmlAttribute
    private Long id;

    @Column(unique = true)
    private String title;

    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime date;
    private BigDecimal ticketPrice = BigDecimal.ZERO;

    @OneToMany(mappedBy = "event")
    private List<Ticket> tickets;

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

    public List<Ticket> getTickets() {
        return tickets;
    }

    @Override
    public String toString() {
        return "Event [id=" + id + ", title=" + title + ", date=" + date + ", price=" + ticketPrice + "]";
    }

}
