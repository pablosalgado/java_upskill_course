package com.epam.pablo.task05.service.impl;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.epam.pablo.task05.service.Query;

@SpringBootTest
public class QueryImplTests {

    @Autowired
    private Query query;

        @MockBean
    private CommandLineRunner runner;

    @Test
    public void testFindUsersWithManyFriendsAndLikes() {
        // Define the date parameters
        Date friendsBeforeDate = Date.valueOf("2025-04-01");
        Date likesStartDate = Date.valueOf("2025-03-01");
        Date likesEndDate = Date.valueOf("2025-04-01");

        // Execute the method
        List<String[]> results = query.findUsersWithManyFriendsAndLikes(friendsBeforeDate, likesStartDate, likesEndDate);

        // Assertions
        assertThat(results).isNotNull();
        assertThat(results).hasSize(74); // Check that the result contains exactly 74 rows

        // Check the first row specifically
        assertThat(results.get(0)).containsExactly("Abigail", "Walker", "121", "105");
        assertThat(results.get(1)).containsExactly("Abigail", "Young", "135", "101");
    }
}