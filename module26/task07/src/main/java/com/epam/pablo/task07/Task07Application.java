package com.epam.pablo.task07;

import java.io.File;
import java.nio.file.Files;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.epam.pablo.task07.service.FileService;
import com.epam.pablo.task07.util.DatabaseSeeder;

@SpringBootApplication
public class Task07Application implements CommandLineRunner{

	private Logger logger = Logger.getLogger(Task07Application.class.getName());

	@Autowired
	private DatabaseSeeder databaseSeeder;

	@Autowired FileService fileService;

	public static void main(String[] args) {
		SpringApplication.run(Task07Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("Application started");

		databaseSeeder.seed();

		if (args.length > 0 ) {
			logger.info("Uploading file: " + args[0]);
			
			var currentDir = System.getProperty("user.dir");
			var fileName = args[0];
			var file = new File(fileName);
	
			if (!file.isAbsolute()) {
				file = new File(currentDir, fileName);
			}

			byte[] bytes = Files.readAllBytes(file.toPath());

			databaseSeeder.uploadFile(file.getName(), bytes);
		}

		logger.info("Application finished");
	}
}
