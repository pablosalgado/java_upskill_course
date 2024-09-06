package com.epam.pablo.task02;

import java.io.File;
import org.springframework.boot.CommandLineRunner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.epam.pablo.task02.storage.DynamicTableGenerator;

@SpringBootApplication
public class Task02Application implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(Task02Application.class);
	private final DynamicTableGenerator tableGenerator;

	public Task02Application(DynamicTableGenerator tableGenerator) {
		this.tableGenerator = tableGenerator;
	}

	public static void main(String[] args) {
		SpringApplication.run(Task02Application.class, args);
	}

	@Override
	public void run(String... args) {
		try {logger.info("Application started");

			var configurationFile = determineFilePath(args);
			tableGenerator.createAndPopulateTables(configurationFile);
			
			logger.info("Application finished");
		} catch (Exception e) {
			logger.error("Error during execution: {}", e.getMessage(), e);
		}
	}

	private File determineFilePath(String[] args) {
		var currentDir = System.getProperty("user.dir");
		File file;

		if (args.length == 0) {
			// If no arguments are provided, use the default configuration file
			file = new File(currentDir, "config.json");
            logger.info("Using default configuration file: {}", file.getAbsolutePath());
		} else {
			// If an argument is provided, use it as the configuration file
			var filePath = args[0];
			file = new File(filePath);
	
			if (!file.isAbsolute()) {
				file = new File(currentDir, filePath);
			}

            logger.info("Using provided configuration file: {}", file.getAbsolutePath());
		}

		return file;
	}
}
