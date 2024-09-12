package com.epam.pablo.task01;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.epam.pablo.task01.facade.BookingFacade;
import com.epam.pablo.task01.model.factory.EventFactory;
import com.epam.pablo.task01.model.factory.UserFactory;
import com.epam.pablo.task01.model.Ticket;
import com.epam.pablo.task01.model.User;
import com.epam.pablo.task01.model.Event;

@SpringBootApplication
public class Task01Application implements CommandLineRunner {

	@Autowired
	private BookingFacade bookingFacade;

	@Autowired
	private UserFactory userFactory;

	@Autowired
	private EventFactory eventFactory;

	private User johnDoe;
	private User janeSmith;
	private User aliceJohnson;
	private User bobBrown;
	private User charlieDavis;
	private Event conference;
	private Event workshop;
	private Event seminar;
	private Event webinar;

	public static void main(String[] args) {
		SpringApplication.run(Task01Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		createUsers();
		createEvents();
		bookTickets();
	}

	private void createUsers() {
		System.out.println("Creating users...");

		System.out.println("Creating John Doe...");
		johnDoe = bookingFacade.createUser(
				userFactory.createUser("John Doe", "john.doe@example.com", BigDecimal.valueOf(100)));

		System.out.println("Creating Jane Smith...");
		janeSmith = bookingFacade.createUser(
				userFactory.createUser("Jane Smith", "jane.smith@example.com", BigDecimal.valueOf(50)));

		System.out.println("Creating Alice Johnson...");
		aliceJohnson = bookingFacade.createUser(
				userFactory.createUser("Alice Johnson", "alice.johnson@example.com", BigDecimal.valueOf(75)));

		System.out.println("Creating Bob Brown...");
		bobBrown = bookingFacade.createUser(
				userFactory.createUser("Bob Brown", "bob.brown@example.com", BigDecimal.valueOf(125)));

		System.out.println("Creating Charlie Davis...");
		charlieDavis = bookingFacade.createUser(
				userFactory.createUser("Charlie Davis", "charlie.davis@example.com", BigDecimal.valueOf(150)));

		System.out.println("Users created.");
	}

	private void createEvents() {
		System.out.println("Creating events...");

		System.out.println("Creating Conference...");
		conference = bookingFacade.createEvent(
				eventFactory.createEvent(
						"Conference",
						Date.from(LocalDate.of(2023, 5, 15).atStartOfDay().toInstant(ZoneOffset.UTC)),
						BigDecimal.valueOf(100)));

		System.out.println("Creating Workshop...");
		workshop = bookingFacade.createEvent(
				eventFactory.createEvent(
						"Workshop",
						Date.from(LocalDate.of(2023, 6, 20).atStartOfDay().toInstant(ZoneOffset.UTC)),
						BigDecimal.valueOf(75)));

		System.out.println("Creating Seminar...");
		seminar = bookingFacade.createEvent(
				eventFactory.createEvent(
						"Seminar",
						Date.from(LocalDate.of(2023, 7, 10).atStartOfDay().toInstant(ZoneOffset.UTC)),
						BigDecimal.valueOf(60)));

		System.out.println("Creating Webinar...");
		webinar = bookingFacade.createEvent(
				eventFactory.createEvent(
						"Webinar",
						Date.from(LocalDate.of(2023, 8, 5).atStartOfDay().toInstant(ZoneOffset.UTC)),
						BigDecimal.valueOf(40)));

		System.out.println("Events created.");
	}

	private void bookTickets() {
		System.out.println("Booking tickets...");

		System.out.println("\n\n----------------------------------------");
		System.out.println("John Doe booking tickets...");
		System.out.println(
				"John Doe initial account balance: $" + bookingFacade.getUserById(johnDoe.getId()).getAccountBalance());
		System.out.printf("John Doe wants to book a ticket for the %s at $%s%n", conference.getTitle(),
				conference.getTicketPrice().toString());

		bookingFacade.bookTicket(johnDoe.getId(), conference.getId(), 1, Ticket.Category.PREMIUM);

		System.out.println("Ticket booked.");
		System.out.println("John Doe account balance after booking: $"
				+ bookingFacade.getUserById(johnDoe.getId()).getAccountBalance());

		try {
			System.out.printf("John Doe wants to book another ticket for the %s at $%s%n",
					conference.getTitle(), conference.getTicketPrice().toString());
			bookingFacade.bookTicket(johnDoe.getId(), conference.getId(), 1, Ticket.Category.PREMIUM);
		} catch (Exception e) {
			System.out.println("Error booking ticket: " + e.getMessage());
		}

		System.out.println("\n\n----------------------------------------");
		System.out.println("Jane Smith booking tickets...");
		System.out.println("Jane Smith initial account balance: $"
				+ bookingFacade.getUserById(janeSmith.getId()).getAccountBalance());
		System.out.printf("Jane Smith wants to book a ticket for the %s at $%s%n", workshop.getTitle(),
				workshop.getTicketPrice().toString());
		try {
			bookingFacade.bookTicket(janeSmith.getId(), workshop.getId(), 2, Ticket.Category.STANDARD);
		} catch (Exception e) {
			System.out.println("Error booking ticket: " + e.getMessage());
		}

		System.out.println("\n\n----------------------------------------");
		System.out.println("Alice Johnson booking tickets...");
		System.out.println("Alice Johnson initial account balance: $"
				+ bookingFacade.getUserById(aliceJohnson.getId()).getAccountBalance());
		System.out.printf("Alice Johnson wants to book a ticket for the %s at $%s%n", seminar.getTitle(),
				seminar.getTicketPrice().toString());

		var ticket = bookingFacade.bookTicket(aliceJohnson.getId(), seminar.getId(), 3, Ticket.Category.STANDARD);

		System.out.println("Ticket booked.");
		System.out.println("Alice Johnson account balance after booking: $"
				+ bookingFacade.getUserById(aliceJohnson.getId()).getAccountBalance());
		System.out.println("Alice Johnson  wants to cancel the ticket...");

		bookingFacade.cancelTicket(ticket.getId());

		System.out.println("Ticket canceled.");
		System.out.println("Alice Johnson account balance after canceling: $"
				+ bookingFacade.getUserById(aliceJohnson.getId()).getAccountBalance());

		System.out.println("\n\n----------------------------------------");
		System.out.println("Bob Brown booking tickets...");
		System.out.println("Bob Brown initial account balance: $"
				+ bookingFacade.getUserById(bobBrown.getId()).getAccountBalance());
		System.out.printf("Bob Brown wants to book a ticket for the %s at $%s%n", webinar.getTitle(),
				webinar.getTicketPrice().toString());

		bookingFacade.bookTicket(bobBrown.getId(), webinar.getId(), 4, Ticket.Category.PREMIUM);

		System.out.println("Ticket booked.");
		System.out.println("Bob Brown account balance after booking: $"
				+ bookingFacade.getUserById(bobBrown.getId()).getAccountBalance());
		System.out.println("Bob Brown wants to refill the account balance with $100...");

		bookingFacade.refillAccount(bobBrown.getId(), BigDecimal.valueOf(100));

		System.out.println("Bob Brown account balance after refill: $"
				+ bookingFacade.getUserById(bobBrown.getId()).getAccountBalance());
		System.out.println("Tickets booked.");
	}
}
