package com.epam.pablo.task01.integration;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.boot.test.context.SpringBootTest;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.repository.EventRepository;
import com.epam.pablo.task01.repository.TicketRepository;
import com.epam.pablo.task01.repository.UserRepository;
import com.epam.pablo.task01.service.impl.ImportDataServiceImpl;

import java.nio.file.Path;

@SpringBootTest
public class ImportDataServiceImplIntegrationTest {

    @Autowired
    private ImportDataServiceImpl importDataService;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventRepository eventRepository;

    @Test
    public void testPreloadTickets(@TempDir Path tempDir) throws Exception {
        importDataService.preloadTickets();

        assertThat(ticketRepository.count()).isGreaterThan(0);
        assertThat(userRepository.count()).isGreaterThan(0);
        assertThat(eventRepository.count()).isGreaterThan(0);

        assertThat(userRepository.findAll().stream().map(User::getEmail).distinct().count())
            .isEqualTo(userRepository.count());
        assertThat(eventRepository.findAll().stream().map(Event::getTitle).distinct().count())
            .isEqualTo(eventRepository.count());
    }
}