package com.epam.pablo.task02.storage.impl;

import com.epam.pablo.task02.config.Configuration;
import com.epam.pablo.task02.config.ConfigurationLoadException;
import com.epam.pablo.task02.config.TableConfig;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.epam.pablo.task02.storage.DynamicTable;
import com.epam.pablo.task02.storage.DynamicTableGenerator;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit; 

@Component
public class DynamicTableGeneratorImpl implements DynamicTableGenerator {
    private static final Logger logger = LoggerFactory.getLogger(DynamicTableGeneratorImpl.class);

    private final ApplicationContext context;
    private final ObjectMapper mapper;
    private final JdbcTemplate jdbcTemplate;

    public DynamicTableGeneratorImpl(ApplicationContext context, ObjectMapper mapper, JdbcTemplate jdbcTemplate) {
        this.context = context;
        this.mapper = mapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void createAndPopulateTables(File config) {
        var configuration = loadConfiguration(config);
        processConfiguration(configuration);
    }

    private Configuration loadConfiguration(File config) {
        logger.info("Reading configuration from file: {}", config.getAbsolutePath());

        try {
            Configuration configuration = mapper.readValue(config, new TypeReference<Configuration>() {});
            logger.info("Configuration read successfully");
            return configuration;
        } catch (IOException e) {
            var msg = "Failed to read configuration from file: " + config.getAbsolutePath();
            logger.error(msg, e);   
            throw new ConfigurationLoadException(msg, e);
        }
    }

    private void processConfiguration(Configuration configuration) {
        resetDatabase();
        populateDatabase(configuration);
    }

    private void resetDatabase() {
        var url = context.getEnvironment().getProperty("spring.datasource.url");

        logger.info("Resetting database: {}", url);

        List<String> tables = jdbcTemplate.queryForList("SELECT tablename FROM pg_catalog.pg_tables WHERE schemaname='public'", String.class);
        for (String table : tables) {
            jdbcTemplate.execute("DROP TABLE " + table);
            logger.info("Table dropped: {}", table);
        }
    }

    private void populateDatabase(Configuration configuration) {
        var threads = configuration.getConcurrency();
        var executor = createExecutorService(threads);
        var futures = submitTableCreationTasks(configuration, executor);
        waitForCompletion(futures);
        shutdownExecutorService(executor);
   }

    private ExecutorService createExecutorService(int threads) {
        return Executors.newFixedThreadPool(threads);
    }

    private List<Future<?>> submitTableCreationTasks(Configuration configuration, ExecutorService executor) {
        var futures = new ArrayList<Future<?>>();

        for (var tableConfig : configuration.getTableConfigList()) {
            var future = executor.submit(() -> createAndPopulateTable(tableConfig));
            futures.add(future);
        }

        return futures;
    }

    private void createAndPopulateTable(TableConfig tableConfig) {
        var table = context.getBean(DynamicTable.class);
        table.createAndPopulate(tableConfig);
    }

    private void waitForCompletion(List<Future<?>> futures) {
        for (var future : futures) {
            try {
                future.get();
            } catch (Exception e) {
                logger.error("Error during table creation: {}", e.getMessage(), e);
            }
        }
    }

    private void shutdownExecutorService(ExecutorService executor) {
        executor.shutdown();

        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException ie) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
