package com.epam.pablo.task05.service;

import java.sql.Date;
import java.util.List;

public interface Query {

     List<String[]> findUsersWithManyFriendsAndLikes(
        Date friends_before_date, 
        Date likes_start_date, 
        Date likes_end_date 
    );

}
