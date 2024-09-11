package com.epam.pablo.task01.integration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.pablo.exception.EventNotFoundException;
import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.Event;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.model.factory.EventFactory;
import com.epam.pablo.task01.model.factory.UserFactory;

@SpringBootTest
class BookingIntegrationTest {

    @MockBean
    private CommandLineRunner runner;

    @Autowired
    private BookingFacade bookingFacade;

    @Autowired
    private EventFactory eventFactory;

    @Autowired
    private UserFactory userFactory;

    @Test
    public void testCreateAndRetrieveEvent() {
        Event createdEvent = createEvent("Test Event", new Date());

        Event retrievedEvent = bookingFacade.getEventById(createdEvent.getId());

        assertNotNull(retrievedEvent);
        assertEquals("Test Event", retrievedEvent.getTitle());
    }

    @Test
    public void testCreateAndRetrieveUser() {
        User createdUser = createUser("John Doe", "john.doe@example.com");
        User retrievedUser = bookingFacade.getUserById(createdUser.getId());

        assertNotNull(retrievedUser);
        assertEquals("john.doe@example.com", retrievedUser.getEmail());
        assertEquals(0, retrievedUser.getAccountBalance().doubleValue());
    }

    @Test
    public void testBookTicketWhenUserHasEnoughMoney() {
        User createdUser = createUser("Jane Doe", "jane.doe@example.com", BigDecimal.valueOf(100));
        Event createdEvent = createEvent("Concert", new Date(), BigDecimal.valueOf(50));

        Ticket ticket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 1, Ticket.Category.PREMIUM);

        assertNotNull(ticket);
        assertEquals(new BigDecimal("50.00"), ticket.getUser().getAccountBalance());
    }

    @Test
    public void testBookTicketWhenUserHasInsufficientMoney() {
        User createdUser = createUser("Jane Doe", "jane.doe@example.com", BigDecimal.TEN);
        Event createdEvent = createEvent("Concert", new Date(), BigDecimal.valueOf(50));

        assertThrows(Exception.class, () -> {
            bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 1, Ticket.Category.PREMIUM);
        });
    }

    @Test
    public void testBookAndCancelTicket() {
        User createdUser = createUser("Jane Doe", "jane.doe@example.com", BigDecimal.valueOf(100));
        Event createdEvent = createEvent("Concert", new Date(), BigDecimal.valueOf(50));

        Ticket ticket = bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 1, Ticket.Category.PREMIUM);
        assertNotNull(ticket);

        assertEquals(new BigDecimal("50.00"), bookingFacade.getUserById(createdUser.getId()).getAccountBalance());

        boolean canceled = bookingFacade.cancelTicket(ticket.getId());
        assertTrue(canceled);

        assertEquals(new BigDecimal("100.00"), bookingFacade.getUserById(createdUser.getId()).getAccountBalance());
    }

    @Test
    public void testGetBookedTickets() {
        User createdUser = createUser("Alice", "alice@example.com");
        Event createdEvent = createEvent("Seminar", new Date());

        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 1, Ticket.Category.STANDARD);
        bookingFacade.bookTicket(createdUser.getId(), createdEvent.getId(), 2, Ticket.Category.STANDARD);

        List<Ticket> tickets = bookingFacade.getBookedTickets(createdUser, 10, 0);
        assertEquals(2, tickets.size());
    }

    @Test
    void shouldBookAndCancelTicketSuccessfully() {
        User user = createUser("John Doe", "john.doe@example.com");
        Event event = createEvent("Concert", new Date());

        Ticket ticket = bookingFacade.bookTicket(user.getId(), event.getId(), 1, Ticket.Category.PREMIUM);

        assertNotNull(ticket, "Ticket should not be null");
        assertNotNull(ticket.getId(), "Ticket ID should not be null");

        boolean cancelStatus = bookingFacade.cancelTicket(ticket.getId());

        assertTrue(cancelStatus, "Ticket should be cancelled successfully");
    }

    @Test
    void shouldThrowExceptionWhenBookingTicketForNonExistentEvent() {
        User user = createUser("Jane Doe", "jane.doe@example.com");

        assertThrows(EventNotFoundException.class,
                () -> bookingFacade.bookTicket(user.getId(), 999L, 1, Ticket.Category.PREMIUM),
                "Should throw EventNotFoundException for non-existent event");
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        User user = createUser("Alice Smith", "alice.smith@example.com");

        user.setName("John Johnson");
        User updatedUser = bookingFacade.updateUser(user);

        assertEquals("John Johnson", updatedUser.getName(), "User name should be updated");
    }

    @Test
    void shouldRetrieveUsersWithPagination() {
        List<User> users = bookingFacade.getUsersByName("John", 10, 0);

        assertNotNull(users, "Users list should not be null");
        assertTrue(users.size() <= 10, "Users list should not exceed page size");
    }

    @Test
    void shouldRetrieveEventsWithPagination() {
        List<Event> events = bookingFacade.getEventsByTitle("Concert", 10, 0);

        assertNotNull(events, "Events list should not be null");
        assertTrue(events.size() <= 10, "Events list should not exceed page size");
    }

    private User createUser(String name, String email) {
        User user = userFactory.createUser(name, email);
        return bookingFacade.createUser(user);
    }

    private User createUser(String name, String email, BigDecimal accountBalance) {
        User user = userFactory.createUser(name, email, accountBalance);
        return bookingFacade.createUser(user);
    }

    private Event createEvent(String title, Date date) {
        Event event = eventFactory.createEvent(title, date);
        return bookingFacade.createEvent(event);
    }

    private Event createEvent(String title, Date date, BigDecimal ticketPrice) {
        Event event = eventFactory.createEvent(title, date, ticketPrice);
        return bookingFacade.createEvent(event);
    }
}
