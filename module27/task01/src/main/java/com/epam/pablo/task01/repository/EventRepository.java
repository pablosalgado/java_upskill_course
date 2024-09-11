package com.epam.pablo.task01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.epam.pablo.task01.model.Event;

import java.util.Date;

public interface EventRepository extends JpaRepository<Event, Long> {

    Page<Event> findByTitleContaining(String title, Pageable pageable);

    Page<Event> findByDate(Date date, Pageable pageable);

}