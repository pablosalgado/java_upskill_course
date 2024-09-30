package com.epam.pablo.task01.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.epam.pablo.task01.model.User;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    Page<User> findByName(String name, Pageable pageable);

    @Query("select u from User u left join fetch u.userAccount")
    Page<User> findAllWithUserAccount(Pageable pageable);

}
