/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.post;

import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.post.*;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.list.MovieListRepository;
import com.moviePocket.db.repository.movie.MovieRepository;
import com.moviePocket.db.repository.post.*;
import com.moviePocket.db.repository.review.LikeReviewRepository;
import com.moviePocket.db.repository.review.ReviewPostRepository;
import com.moviePocket.db.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.post.PostServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class PostServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @Mock
    private PostListRepository postListRepository;

    @Mock
    private PostMovieRepository postMovieRepository;

    @Mock
    private PostPersonRepository postPersonRepository;

    @Mock
    private ReviewPostRepository reviewPostRepository;

    @Mock
    private LikePostRepository likePostRepository;

    @Mock
    private LikeReviewRepository likeReviewRepository;

    @InjectMocks
    private PostServiceImpl postService;
    @Mock
    private MovieRepository movieRepository;
    @Mock
    private MovieListRepository movieListRepository;
    @InjectMocks
    private MovieServiceImpl movieService;

    @Test
    void testCreatePostListSuccess() {
        // Mock data
        String email = "test@example.com";
        String title = "Test Title";
        String content = "Test Content";
        Long idList = 1L;

        // Mock user
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        ListMovie movieList = new ListMovie();
        when(movieListRepository.getById(idList)).thenReturn(movieList);

        // Test service method
        ResponseEntity<ParsPost> response = postService.createPostList(email, title, content, idList);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

//    @Test
//    void testCreatePostMovieSuccess() {
//        // Mock data
//        String email = "test@example.com";
//        String title = "Test Title";
//        String content = "Test Content";
//        Long idMovie = 1L;
//
//        // Mock user
//        User user = new User();
//        when(userRepository.findByEmail(email)).thenReturn(user);
//
//        // Mock repository methods
//        when(movieRepository.existsById(idMovie)).thenReturn(true);
//
//        when(movieService.setMovie(idMovie)).thenReturn(new Movie());
//
//        // Test service method
//        ResponseEntity<ParsPost> response = postService.createPostMovie(email, title, content, idMovie);
//
//        // Assertions
//        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
//    }

    @Test
    void testCreatePostPersonSuccess() {
        // Mock data
        String email = "test@example.com";
        String title = "Test Title";
        String content = "Test Content";
        Long idPerson = 1L;

        // Mock user
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Test service method
        ResponseEntity<ParsPost> response = postService.createPostPerson(email, title, content, idPerson);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdatePostSuccess() {
        // Mock data
        String email = "test@example.com";
        Long idPost = 1L;
        String title = "Updated Title";
        String content = "Updated Content";

        // Mock user
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        Post post = new Post(user, "Original Title", "Original Content");
        when(postRepository.getById(idPost)).thenReturn(post);

        // Test service method
        ResponseEntity<Void> response = postService.updatePost(email, idPost, title, content);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(title, post.getTitle());
        assertEquals(content, post.getContent());
    }

    @Test
    void testUpdatePostUnauthorized() {
        // Mock data
        String email = "test@example.com";
        Long idPost = 1L;
        String title = "Updated Title";
        String content = "Updated Content";

        // Mock user (null, simulating unauthorized)
        when(userRepository.findByEmail(email)).thenReturn(null);

        // Test service method
        ResponseEntity<Void> response = postService.updatePost(email, idPost, title, content);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // Similar tests for other scenarios...

    @Test
    void testDeletePostSuccess() {
        // Mock data
        String email = "test@example.com";
        Long idPost = 1L;

        // Mock user
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);

        // Mock repository methods
        Post post = new Post(user, "Original Title", "Original Content");
        post.setUser(user);
        when(postRepository.getById(idPost)).thenReturn(post);
        when(reviewPostRepository.findReviewsByPost(post)).thenReturn(null);

        // Test service method
        ResponseEntity<Void> response = postService.deletePost(email, idPost);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }


    @Test
    void testGetAllByIdMovieSuccess() {
        // Mock data
        Long idMovie = 1L;

        // Mock repository methods
        List<PostMovie> list = new ArrayList<>();
        when(postMovieRepository.findAllByMovie_Id(idMovie)).thenReturn(list);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByIdMovie(idMovie);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllByIdPersonSuccess() {
        // Mock data
        Long idPerson = 1L;

        // Mock repository methods
        List<PostPerson> list = new ArrayList<>();
        when(postPersonRepository.findAllByIdPerson(idPerson)).thenReturn(list);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByIdPerson(idPerson);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllByUserSuccess() {
        // Mock data
        String email = "test@example.com";

        // Mock repository methods
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAllByUser(user)).thenReturn(posts);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByUser(email);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllByIdListSuccess() {
        // Mock data
        Long idList = 1L;

        // Mock repository methods
        List<PostList> list = new ArrayList<>();
        when(postListRepository.findAllByMovieList_Id(idList)).thenReturn(list);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByIdList(idList);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetAllByTitleSuccess() {
        // Mock data
        String title = "Test Title";

        // Mock repository methods
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAllByTitle(title)).thenReturn(posts);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByTitle(title);

        // Assertions
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAllByPartialTitleWithTitleEmpty() {
        // Mock data
        String title = "";

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByPartialTitle(title);

        // Assertions
        assertNotNull(response);
        assertNull(response.getBody());
    }

    @Test
    void testGetAllByPartialTitleWithTitleNotEmpty() {
        // Mock data
        String title = "Test Title";

        // Mock repository methods
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAllByPartialTitle(title)).thenReturn(posts);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByPartialTitle(title);

        // Assertions
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetPostSuccess() {
        // Mock data
        Long idPost = 1L;

        // Mock repository methods
        Post post = new Post();
        post.setUser(new User());
        when(postRepository.existsById(idPost)).thenReturn(true);
        when(postRepository.getById(idPost)).thenReturn(post);

        // Test service method
        ResponseEntity<ParsPost> response = postService.getPost(idPost);

        // Assertions
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetPostNotFound() {
        // Mock data
        Long idPost = 1L;

        // Mock repository methods
        when(postRepository.existsById(idPost)).thenReturn(false);

        // Test service method
        ResponseEntity<ParsPost> response = postService.getPost(idPost);

        // Assertions
        assertNotNull(response);
        assertNull(response.getBody());
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetAllMyPostsSuccess() {
        // Mock data
        String email = "test@example.com";

        // Mock repository methods
        User user = new User();
        when(userRepository.findByEmail(email)).thenReturn(user);
        when(postRepository.findAllByUser(user)).thenReturn(new ArrayList<>());

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllMyPosts(email);

        // Assertions
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetAllByUsernamePostsSuccess() {
        // Mock data
        String username = "testUser";

        // Mock repository methods
        User user = new User();
        when(userRepository.findByUsernameAndAccountActive(username, true)).thenReturn(user);
        when(postRepository.findAllByUser(user)).thenReturn(new ArrayList<>());

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getAllByUsernamePosts(username);

        // Assertions
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetNewestPostsSuccess() {
        // Mock repository methods
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAll()).thenReturn(posts);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getNewestPosts();

        // Assertions
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetOldestPostsSuccess() {
        // Mock repository methods
        List<Post> posts = new ArrayList<>();
        when(postRepository.findAll()).thenReturn(posts);

        // Test service method
        ResponseEntity<List<ParsPost>> response = postService.getOldestPosts();

        // Assertions
        assertNotNull(response);
        assertTrue(response.getBody().isEmpty());
    }

    // Add more tests for other methods if needed...

}

