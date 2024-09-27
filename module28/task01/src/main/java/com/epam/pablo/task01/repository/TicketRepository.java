package com.epam.pablo.task01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epam.pablo.task01.model.Ticket;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findByUserId(Long userId, Pageable pageable);

    @Query("select t from Ticket t left join fetch t.user left join fetch t.event where t.user.id = :userId")
    Page<Ticket> findByUserIdWithUserAndEvent(Long userId, Pageable pageable);

    Page<Ticket> findByEventId(Long eventId, Pageable pageable);

    @Query("select t from Ticket t left join fetch t.user left join fetch t.event")
    Page<Ticket> findAllWithUserAndEvent(Pageable pageable);

}