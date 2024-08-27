package com.epam.pablo.task01.components;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class Query {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<String[]> findUsersWithManyFriendsAndLikes() {
        var sql = """
            WITH friends_count AS (
                SELECT
                    person.id,
                    person.name,
                    person.surname,
                    COUNT(*) AS friends
                FROM
                    users person
                INNER JOIN friendships fs ON
                    fs.userId1 = person.id
                WHERE
                    fs.timestamp < '2025-04-01'
                GROUP BY 
                    person.id,
                    person.name,
                    person.surname
                HAVING
                    COUNT(fs.userId2) > 100
            ),
            likes_count AS (
                SELECT
                    person.id,
                    person.name,
                    person.surname,
                    COUNT(*) AS likes
                FROM 
                    users person
                INNER JOIN likes ON
                    likes.userId = person.id
                WHERE
                    likes.timestamp BETWEEN '2025-03-01' AND '2025-04-01'
                GROUP BY 
                    person.id,
                    person.name,
                    person.surname
                HAVING
                    COUNT(likes.postId) > 100
            )
            SELECT
                f.name,
                f.surname,
                f.friends,
                l.likes
            FROM
                friends_count f
            INNER JOIN likes_count l ON
                f.id = l.id
            ORDER BY
                f.name,
                f.surname;
            """;

        return jdbcTemplate.query(sql, (rs, rowNum) -> new String[]{
            rs.getString("name"),
            rs.getString("surname"),
            rs.getString("friends"),
            rs.getString("likes")
        });
    }
}
