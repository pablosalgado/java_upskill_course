/**
 * The UserDao class is responsible for interacting with the database to perform CRUD operations on User objects.
 * It uses Spring JDBC and DML (Data Manipulation Language) statements to execute SQL queries and updates.
 * 
 * This class provides the following methods:
 * - findAll: Retrieves a list of all users from the database.
 * - findById: Retrieves a user by their ID from the database.
 * - create: Creates a new user in the database.
 * - update: Updates an existing user in the database.
 * - delete: Deletes a user from the database.
 * 
 * Note: This example uses DML (Data Manipulation Language) statements to execute SQL queries and updates.
 */
package com.epam.pablo.task05.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.epam.pablo.task05.model.Post;

@Repository
public class PostDao {

    @Autowired
    JdbcTemplate jdbcTemplate;

    private static final class PostMapper implements org.springframework.jdbc.core.RowMapper<Post> {
        public Post mapRow(java.sql.ResultSet rs, int rowNum) throws java.sql.SQLException {
            Post post = new Post();
            post.setId(rs.getInt("id"));
            post.setUserId(rs.getInt("userid"));
            post.setText(rs.getString("text"));
            post.setTimestamp(rs.getTimestamp("timestamp"));
            return post;
        }
    }

    public List<Post> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM posts;", 
            new PostMapper()
        );
    }

    public Post findById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM posts WHERE id = ?", 
                new Object[]{id}, 
                new int[]{java.sql.Types.INTEGER},
                new PostMapper()
            );
        } catch (org.springframework.dao.EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public Post create(Post post) {
        final String sql = "INSERT INTO posts (userid, text, timestamp) VALUES (?, ?, ?) RETURNING id";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, post.getUserId());
            ps.setString(2, post.getText());
            ps.setTimestamp(3, post.getTimestamp());
            return ps;
        }, keyHolder);

        post.setId(keyHolder.getKey().intValue());

        return post;
    }    

    public Post update(Post post) {
        jdbcTemplate.update(
            "UPDATE posts SET userid = ?, text = ?, timestamp = ? WHERE id = ?",
            post.getUserId(),
            post.getText(),
            post.getTimestamp(),
            post.getId()
        );

        return post;
    }

    public void delete(Integer id) {
        jdbcTemplate.update(
            "DELETE FROM posts WHERE id = ?",
            id
        );
    }
}
