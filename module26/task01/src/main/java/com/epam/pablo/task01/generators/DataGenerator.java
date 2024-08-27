/**
 * NOTE: This class is not used in the application. Use it if you want to 
 * regenerate the data.
 */
package com.epam.pablo.task01.generators;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.nio.file.Paths;

public class DataGenerator {
    private static Random random = new Random();

    private static final int NUM_USERS = 1600;
    private static final int NUM_POSTS = 100000;
    private static final int NUM_FRIENDSHIPS = 300000;
    private static final int NUM_LIKES = 400000;
    private static final String START_DATE = "2025-02-01 00:00:00";
    private static final String END_DATE = "2025-04-31 23:59:59";
    private static final String[] WORDS = {"lorem", "ipsum", "dolor", "sit", "amet", "consectetur", "adipiscing", "elit"};
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final String[] FIRST_NAMES = {
        "John", "Jane", "Michael", "Emily", "David", "Olivia", "James", "Sophia",
        "Robert", "Isabella", "William", "Mia", "Joseph", "Charlotte", "Thomas", "Amelia",
        "Charles", "Evelyn", "Daniel", "Abigail", "Matthew", "Harper", "Anthony", "Avery",
        "Mark", "Ella", "Paul", "Scarlett", "Steven", "Grace", "Andrew", "Chloe",
        "Joshua", "Victoria", "Pablo", "Lucas", "Gabriel", "Sofia", "Carlos", "Valeria",
    };

    private static final String[] SURNAMES = {
        "Smith", "Johnson", "Williams", "Brown", "Jones", "Garcia", "Miller", "Davis",
        "Rodriguez", "Martinez", "Hernandez", "Lopez", "Gonzalez", "Wilson", "Anderson",
        "Thomas", "Taylor", "Moore", "Jackson", "Martin", "Lee", "Perez", "Thompson",
        "White", "Harris", "Sanchez", "Clark", "Ramirez", "Lewis", "Robinson", "Walker",
        "Young", "Allen", "King", "Salgado", "Gomez", "Flores", "Vargas", "Munoz", "Molina"
    };

    public static void main(String[] args) {
        generateUsers();
        generatePosts();
        generateFriendships();
        generateLikes();
    }

    private static void generateUsers() {
        var filePath = Paths.get("src/main/resources/users.csv").toAbsolutePath().normalize().toString();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (var firstName : FIRST_NAMES) {
                for (var surname : SURNAMES) {
                    String birthDate = generateRandomBirthDate();
                    writer.write(String.format("%s,%s,%s\n", firstName, surname, birthDate));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generatePosts() {
        var filePath = Paths.get("src/main/resources/posts.csv").toAbsolutePath().normalize().toString();

        try (FileWriter writer = new FileWriter(filePath)) {
            for (int i = 1; i <= NUM_POSTS; i++) {
                int userId = new Random().nextInt(NUM_USERS) + 1;
                String text = generateRandomText(WORDS);
                String timestamp = generateRandomTimestamp();
                writer.write(String.format("%d,%s,%s\n", userId, text, timestamp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateFriendships() {
        var filePath = Paths.get("src/main/resources/friendships.csv").toAbsolutePath().normalize().toString();        

        // Generate all possible pairs of unique user IDs.
        List<int[]> pairs = new ArrayList<int[]>();
        for (var i = 1; i <= NUM_USERS; i++) {
            for (var j = i; j <= NUM_USERS; j++) {
                if (i == j) {
                    continue;
                }
                pairs.add(new int[]{i, j});
            }
        }

        // Shuffle the pairs to get random friendships.
        Collections.shuffle(pairs);
        
        // Write the first NUM_FRIENDSHIPS pairs to the file.
        try(var writer = new FileWriter(filePath)) {
            pairs = pairs.subList(0, NUM_FRIENDSHIPS);
            for(var pair : pairs) {
                var user1 = pair[0];
                var user2 = pair[1];
                var timestamp = generateRandomTimestamp();
                writer.write(String.format("%d,%d,%s\n", user1, user2, timestamp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void generateLikes() {
        var filePath = Paths.get("src/main/resources/likes.csv").toAbsolutePath().normalize().toString();

        List<int[]> pairs = new ArrayList<int[]>();
        for (var i = 1; i <= NUM_USERS; i++) {
            for (var j = 1; j <= NUM_POSTS; j++) {
                pairs.add(new int[]{i, j});
            }
        }

        Collections.shuffle(pairs);

        try (var writer = new FileWriter(filePath)) {
            pairs = pairs.subList(0, NUM_LIKES);
            for(var pair : pairs) {
                var userId = pair[0];
                var postId = pair[1];
                var timestamp = generateRandomTimestamp();
                writer.write(String.format("%d,%d,%s\n", userId, postId, timestamp));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String generateRandomBirthDate() {
        var random = new Random();
        int minDay = (int) LocalDate.of(1970, 1, 1).toEpochDay();
        int maxDay = (int) LocalDate.of(2005, 12, 31).toEpochDay();
        long randomDay = minDay + random.nextInt(maxDay - minDay);
        LocalDate randomDate = LocalDate.ofEpochDay(randomDay);
        return randomDate.toString();
    }

    private static String generateRandomText(String[] words) {
        int wordCount = random.nextInt(10) + 5; // Random text length between 5 and 15 words
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < wordCount; i++) {
            text.append(words[random.nextInt(words.length)]).append(" ");
        }
        return text.toString().trim();
    }

    private static String generateRandomTimestamp() {
        LocalDateTime start = LocalDateTime.parse(START_DATE, formatter);
        LocalDateTime end = LocalDateTime.parse(END_DATE, formatter);

        long startSeconds = start.toEpochSecond(java.time.ZoneOffset.UTC);
        long endSeconds = end.toEpochSecond(java.time.ZoneOffset.UTC);
        long randomSeconds = ThreadLocalRandom.current().nextLong(startSeconds, endSeconds);

        LocalDateTime randomDate = LocalDateTime.ofEpochSecond(randomSeconds, 0, java.time.ZoneOffset.UTC);
        return randomDate.format(formatter);
    }

}