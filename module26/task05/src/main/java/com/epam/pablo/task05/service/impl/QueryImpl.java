package com.epam.pablo.task05.service.impl;

import java.util.List;

import com.epam.pablo.task05.service.Query;
import java.sql.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

@Service
public class QueryImpl implements Query {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public QueryImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<String[]> findUsersWithManyFriendsAndLikes(Date friends_before_date , Date likes_start_date , Date likes_end_date ) {
        String sql = "SELECT * FROM get_user_stats(?, ?, ?)";        

        List<String[]> result = jdbcTemplate.query(
            sql, 
            new Object[]{friends_before_date, likes_start_date, likes_end_date}, 
            new int[]{java.sql.Types.DATE, java.sql.Types.DATE, java.sql.Types.DATE},
            new RowMapper<String[]>() {
            @Override
            public String[] mapRow(ResultSet rs, int rowNum) throws SQLException {
                var name = rs.getString("name");
                var surname = rs.getString("surname");
                var friendsCount = rs.getLong("friends");
                var likesCounts = rs.getLong("likes");

                String[] row = {
                    name, 
                    surname, 
                    String.valueOf(friendsCount),
                    String.valueOf(likesCounts)
                };

                return row;
            }
        });

        return result;
    }
}
