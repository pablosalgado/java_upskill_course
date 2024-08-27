package com.epam.pablo.task01;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.epam.pablo.task01.components.DatabaseSeeder;
import com.epam.pablo.task01.components.Printer;
import com.epam.pablo.task01.components.Query;
import com.epam.pablo.task01.components.SchemaLoader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SpringBootApplication
public class Task01Application implements CommandLineRunner{

	private static final Logger logger = LoggerFactory.getLogger(Task01Application.class);

	@Autowired
	private SchemaLoader schema;

	@Autowired
	private DatabaseSeeder seeder;

	@Autowired
	private Query query;

	@Autowired
	private Printer printer;

	public static void main(String[] args) {
		SpringApplication.run(Task01Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application started");

		schema.load();
		seeder.seed();

		var headers = new String[] { "Name", "Surname", "Friends", "Likes" };
		printer.printTable(
			query.findUsersWithManyFriendsAndLikes(),
			headers
		);
	}

}
