package com.epam.pablo.task05;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.beans.factory.annotation.Autowired;

import com.epam.pablo.task05.dao.PostDao;
import com.epam.pablo.task05.dao.UserDao;

import org.springframework.boot.CommandLineRunner;

import com.epam.pablo.task05.model.Post;
import com.epam.pablo.task05.model.User;
import com.epam.pablo.task05.service.Printer;
import com.epam.pablo.task05.service.Query;


@SpringBootApplication
public class Task05Application implements CommandLineRunner {

	@Autowired
	private UserDao userDao;

	@Autowired
	private PostDao postDao;

	@Autowired
	private ApplicationContext context;

	@Autowired
	private Printer printer;
	
	@Autowired
	private Query query;

	public static void main(String[] args) {
		SpringApplication.run(Task05Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("Printing top 10 users:");
		userDao.findAll().stream().limit(10).forEach(System.out::println);


		System.out.println("Finding user with id 374:");
		var user = userDao.findById(374);
		System.out.println(user);

		System.out.println("Creating new user:");
		user = context.getBean(User.class);
		user.setName("Test");
		user.setSurname("Test");
		user.setBirthdate(java.sql.Date.valueOf("2000-01-01"));		
		user = userDao.create(user);
		System.out.println(user);

		System.out.println("Updating the user:" + user.getId());
		user.setName("Test2");
		user.setSurname("Test2");
		user.setBirthdate(java.sql.Date.valueOf("2000-01-02"));
		user = userDao.update(user);
		System.out.println(user);

		System.out.println("Deleting user with id " + user.getId());
		var id = user.getId();
		userDao.delete(id);

		user = userDao.findById(id);
		if (user == null) {
			System.out.println("User with id " + id + " was deleted");
		} else {
			System.out.println("User with id " + id + " was not deleted");
		}


		System.out.println("Printing top 10 posts:");
		postDao.findAll().stream().limit(10).forEach(System.out::println);

		System.out.println("Finding post with id 374:");
		var post = postDao.findById(374);
		System.out.println(post);

		System.out.println("Creating new post:");
		post = context.getBean(Post.class);
		post.setUserId(1);
		post.setText("Test post");
		post.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
		post = postDao.create(post);

		System.out.println("Updating the post:" + post.getId());
		post.setUserId(2);
		post.setText("Test post 2");
		post.setTimestamp(new java.sql.Timestamp(System.currentTimeMillis()));
		post = postDao.update(post);
		System.out.println(post);

		System.out.println("Deleting post with id " + post.getId());
		var postId = post.getId();
		postDao.delete(postId);

		post = postDao.findById(postId);
		if (post == null) {
			System.out.println("Post with id " + postId + " was deleted");
		} else {
			System.out.println("Post with id " + postId + " was not deleted");
		}

		var headers = new String[] { "Name", "Surname", "Friends", "Likes" };
		printer.printTable(
			query.findUsersWithManyFriendsAndLikes(
				java.sql.Date.valueOf("2025-04-01"), 
				java.sql.Date.valueOf("2025-03-01"),
				java.sql.Date.valueOf("2025-04-01")
			),
			headers
		);

	}
}
