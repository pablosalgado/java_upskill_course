package com.epam.pablo.task01.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.opencsv.CSVReader;

import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class DatabaseSeeder {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseSeeder.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void seed() {
        logger.info("Seeding database");

        seedUsers();
        seedPosts();
        seedFriendships();
        seedLikes();

        var userCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM users", Integer.class);
        logger.info("Total users seeded: " + userCount);

        var postCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM posts", Integer.class);
        logger.info("Total posts seeded: " + postCount);

        var friendshipCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM friendships", Integer.class);
        logger.info("Total friendships seeded: " + friendshipCount);

        var likeCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM likes", Integer.class);
        logger.info("Total likes seeded: " + likeCount);

        logger.info("Database seeded");
    }

    private void seedUsers() {
        Resource resource = new ClassPathResource("users.csv");
        String sqlTemplate = "INSERT INTO users (name, surname, birthdate) VALUES ('%s', '%s', '%s')";


        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                String sql = String.format(sqlTemplate, values[0], values[1], values[2]);
                jdbcTemplate.execute(sql);
            }
            logger.info("Users seeded");
        } catch (Exception e) {
            logger.error("Error reading users file", e);
        }
    }

    private void seedPosts() {
        Resource resource = new ClassPathResource("posts.csv");
        String sqlTemplate = "INSERT INTO posts (userId, text, timestamp) VALUES (%s, '%s', '%s')";

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                String sql = String.format(sqlTemplate, values[0], values[1], values[2]);
                jdbcTemplate.execute(sql);
            }
            logger.info("Posts seeded");
        } catch (Exception e) {
            logger.error("Error reading posts file", e);
        }
    }

    private void seedFriendships() {
        Resource resource = new ClassPathResource("friendships.csv");
        String sqlTemplate = "INSERT INTO friendships (userId1, userId2, timestamp) VALUES (%s, %s, '%s')";

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                String sql = String.format(sqlTemplate, values[0], values[1], values[2]);
                jdbcTemplate.execute(sql);
            }
            logger.info("Friendships seeded");
        } catch (Exception e) {
            logger.error("Error reading friendships file", e);
        }
    }

    private void seedLikes() {
        Resource resource = new ClassPathResource("likes.csv");
        String sqlTemplate = "INSERT INTO likes (userId, postId, timestamp) VALUES (%s, %s, '%s')";

        try (CSVReader csvReader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {
            String[] values;
            while ((values = csvReader.readNext()) != null) {
                String sql = String.format(sqlTemplate, values[0], values[1], values[2]);
                jdbcTemplate.execute(sql);
            }

        } catch (Exception e) {
            logger.error("Error reading likes file", e);
        }
    }
}