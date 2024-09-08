package com.epam.pablo.task05.dao;

import static org.assertj.core.api.Assertions.assertThat;

import java.sql.Timestamp;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.epam.pablo.task05.dao.PostDao;
import com.epam.pablo.task05.model.Post;

@SpringBootTest
public class PostDaoTests {

    @Autowired
    private PostDao postDao;

        @MockBean
    private CommandLineRunner runner;

    @Test
    public void testCreatePost() {
        Post newPost = new Post();
        newPost.setUserId(1);
        newPost.setText("Sample post text");
        newPost.setTimestamp(new Timestamp(System.currentTimeMillis()));

        Post savedPost = postDao.create(newPost);
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getId()).isNotNull();
    }

    @Test
    public void testFindById() {
        Post post = postDao.findById(1);
        assertThat(post).isNotNull();
        assertThat(post.getId()).isEqualTo(1);
    }

    @Test
    public void testFindAll() {
        List<Post> posts = postDao.findAll();
        assertThat(posts).isNotEmpty();
    }

    @Test
    public void testUpdatePost() {
        Post post = postDao.findById(1);
        post.setText("Updated text");
        Post updatedPost = postDao.update(post);
        assertThat(updatedPost.getText()).isEqualTo("Updated text");
    }

    @Test
    public void testDeletePost() {
        // Create a post to delete
        Post newPost = new Post();
        newPost.setUserId(1);
        newPost.setText("Post to delete");
        newPost.setTimestamp(new Timestamp(System.currentTimeMillis()));
        Post createdPost = postDao.create(newPost);

        // Delete the post
        postDao.delete(createdPost.getId());

        // Verify post is deleted
        Post deletedPost = postDao.findById(createdPost.getId());
        assertThat(deletedPost).isNull();
    }
}