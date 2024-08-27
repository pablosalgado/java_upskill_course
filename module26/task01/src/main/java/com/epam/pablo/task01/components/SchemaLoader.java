package com.epam.pablo.task01.components;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class SchemaLoader {

    private static final Logger logger = LoggerFactory.getLogger(SchemaLoader.class);

    @Autowired
    JdbcTemplate jdbcTemplate;

    public void load()
    {
        logger.info("Loading schema");

        createUsersTable();
        createPostsTable();
        createFrienshipsTable();
        createLikesTable();

        addForeignKeyToPosts();
        addForeignKeysToFriendships();
        addForeignKeysToLikes();

        addUniqueIndexToFriendships();
        addUniqueIndexToLikes();

        logger.info("Schema loaded");
    }

    private void createUsersTable() {
        String sql = """
            CREATE TABLE users (
                id INT PRIMARY KEY AUTO_INCREMENT, 
                name VARCHAR(255), 
                surname VARCHAR(255), 
                birthdate DATE
            )
        """;

        jdbcTemplate.execute(sql);

        logger.info("Table users created");
    }

    private void createPostsTable() {
        String sql = """
            CREATE TABLE posts (
                id INT PRIMARY KEY AUTO_INCREMENT,
                userId INT,
                text VARCHAR(255),
                timestamp TIMESTAMP
            )
        """;

        jdbcTemplate.execute(sql);

        logger.info("Table posts created");
    }

    private void createFrienshipsTable() {
        String sql = """
            CREATE TABLE friendships (
                userId1 INT,
                userId2 INT,
                timestamp TIMESTAMP
            )
        """;

        jdbcTemplate.execute(sql);

        logger.info("Table friendships created");
    }

    private void createLikesTable() {
        String sql = """
            CREATE TABLE likes (
                userId INT,
                postId INT,
                timestamp TIMESTAMP
            )
        """;

        jdbcTemplate.execute(sql);

        logger.info("Table likes created");
    }

    private void addForeignKeyToPosts() {
        String sql = """
            ALTER TABLE posts
            ADD CONSTRAINT fk_posts_userId_users
            FOREIGN KEY (userId)
            REFERENCES users(id)
        """;

        jdbcTemplate.execute(sql);

        logger.info("Foreign key added to posts table");
    }

    private void addForeignKeysToFriendships() {
        String sql = """
            ALTER TABLE friendships
            ADD CONSTRAINT fk_friendships_userId1_users
            FOREIGN KEY (userId1)
            REFERENCES users(id)
        """;        

        jdbcTemplate.execute(sql);

        sql = """
            ALTER TABLE friendships
            ADD CONSTRAINT fk_friendships_userId2_users
            FOREIGN KEY (userId2)
            REFERENCES users(id)
        """;

        jdbcTemplate.execute(sql);

        logger.info("Foreign keys added to friendships table");
    }

    private void addForeignKeysToLikes() {
        String sql = """
            ALTER TABLE likes
            ADD CONSTRAINT fk_likes_userId_users
            FOREIGN KEY (userId)
            REFERENCES users(id)
        """;

        jdbcTemplate.execute(sql);

        sql = """
            ALTER TABLE likes
            ADD CONSTRAINT fk_likes_postId_posts
            FOREIGN KEY (postId)
            REFERENCES posts(id)
        """;

        jdbcTemplate.execute(sql);

        logger.info("Foreign keys added to likes table");
    }

    private void addUniqueIndexToFriendships() {
        String sql = """
            CREATE UNIQUE INDEX idx_friendships_userId1_userId2
            ON friendships (userId1, userId2)
        """;

        jdbcTemplate.execute(sql);

        logger.info("Unique index added to friendships table");
    }

    private void addUniqueIndexToLikes() {
        String sql = """
            CREATE UNIQUE INDEX idx_likes_userId_postId
            ON likes (userId, postId)
        """;

        jdbcTemplate.execute(sql);

        logger.info("Unique index added to likes table");
    }
    
}
