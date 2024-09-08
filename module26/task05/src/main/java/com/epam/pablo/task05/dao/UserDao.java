/**
 * The UserDao class is responsible for interacting with the database to perform CRUD operations on User objects.
 * It uses Spring JDBC and stored procedures to execute SQL queries and updates.
 * 
 * This class provides the following methods:
 * - findAll: Retrieves a list of all users from the database.
 * - findById: Retrieves a user by their ID from the database.
 * - create: Creates a new user in the database.
 * - update: Updates an existing user in the database.
 * - delete: Deletes a user from the database.
 * 
 * Note: This example uses stored procedures to execute SQL queries and updates.
 */
package com.epam.pablo.task05.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.epam.pablo.task05.model.User;


@Repository
public class UserDao {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private static final class UserMapper implements RowMapper<User> {
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getInt("id"));
            user.setName(rs.getString("name"));
            user.setSurname(rs.getString("surname"));
            user.setBirthdate(rs.getDate("birthdate"));
            return user;
        }
    }

    public List<User> findAll() {
        return jdbcTemplate.query(
            "SELECT * FROM find_all_users()", 
            new UserMapper()
        );
    }

    public User findById(Integer id) {
        try {
            return jdbcTemplate.queryForObject(
                "SELECT * FROM find_user_by_id(?)", 
                new Object[]{id}, 
                new int[]{java.sql.Types.INTEGER},
                new UserMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null; 
        }
    }

    public User create(User user) {
        var id = jdbcTemplate.queryForObject(
            "SELECT create_user(?, ?, ?)",
            new Object[]{user.getName(), user.getSurname(), user.getBirthdate()},
            new int[]{java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.DATE},
            Integer.class
        );

        return findById(id);
    }

    public User update(User user) {
        jdbcTemplate.update(
            "CALL update_user(?, ?, ?, ?)",
            new Object[]{user.getId(), user.getName(), user.getSurname(), user.getBirthdate()},
            new int[]{java.sql.Types.INTEGER, java.sql.Types.VARCHAR, java.sql.Types.VARCHAR, java.sql.Types.DATE}
        );

        return findById(user.getId());
    }

    public void delete(Integer id) {
        jdbcTemplate.update(
            "CALL delete_user(?)",
            new Object[]{id},
            new int[]{java.sql.Types.INTEGER}
        );
    }
}
