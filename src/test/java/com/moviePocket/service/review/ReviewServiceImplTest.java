package com.moviePocket.service.review;

import com.moviePocket.controller.dto.review.ReviewDTO;
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.post.Post;
import com.moviePocket.db.entities.review.Review;
import com.moviePocket.db.entities.review.ReviewList;
import com.moviePocket.db.entities.review.ReviewMovie;
import com.moviePocket.db.entities.review.ReviewPost;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.review.ReviewListRepository;
import com.moviePocket.db.repository.review.ReviewMovieRepository;
import com.moviePocket.db.repository.review.ReviewPostRepository;
import com.moviePocket.db.repository.review.ReviewRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.list.MovieListServiceImpl;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.post.PostServiceImpl;
import com.moviePocket.service.impl.review.ReviewServiceImpl;
import com.moviePocket.util.ModulesConstant;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReviewServiceImplTest {

    @Mock
    private ReviewRepository reviewRepository;
    @Mock
    private MovieServiceImpl movieService;
    @Mock
    private AuthUser auth;
    @Mock
    private ReviewMovieRepository reviewMovieRepository;
    @Mock
    private MovieListServiceImpl listService;
    @Mock
    private ReviewListRepository reviewListRepository;
    @Mock
    private PostServiceImpl postService;
    @Mock
    private ReviewPostRepository reviewPostRepository;

    @InjectMocks
    private ReviewServiceImpl reviewServiceImpl;


    @Test
    void testCreateReviewMovie_success() {
        // given
        var movie = Movie.builder()
                .id(42L)
                .title("Matrix")
                .build();

        String title = "Cool movie";
        String content = "I liked it a lot.";

        User mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .build();

        Review review = Review.builder()
                .title(title)
                .content(content)
                .user(mockUser)
                .module(ModulesConstant.movie)
                .idItem(movie.getId())
                .reactions(List.of())
                .build();
        review.setId(1L);
        review.setCreated(LocalDateTime.now());
        review.setUpdated(LocalDateTime.now());


        ReviewMovie reviewMovie = ReviewMovie.builder()
                .id(200L)
                .movie(movie)
                .review(review)
                .build();
        reviewMovie.setId(1L);

        // mocks
        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(movieService.getOrSetMovieIfNotExistOrThrowNotFoundException(movie.getId())).thenReturn(movie);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewMovieRepository.save(any(ReviewMovie.class))).thenReturn(reviewMovie);

        // when
        ReviewDTO result = reviewServiceImpl.createReviewMovie(movie.getId(), title, content);

        // then
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(content, result.getContent());
        assertEquals(mockUser.getUsername(), result.getUser().getUsername());

        verify(auth).getAuthenticatedUser();
        verify(movieService).getOrSetMovieIfNotExistOrThrowNotFoundException(movie.getId());
        verify(reviewRepository).save(any());
        verify(reviewMovieRepository).save(any());
    }

    @Test
    void testCreateReviewMovie_badRequest() {

    }


    @Test
    void testCreateReviewPost_success() {
        // given
        var post = Post.builder()
                .title("Matrix")
                .build();

        String title = "Cool movie";
        String content = "I liked it a lot.";

        User mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .build();

        var review = Review.builder()
                .title(title)
                .content(content)
                .user(mockUser)
                .module(ModulesConstant.post)
                .idItem(post.getId())
                .reactions(List.of())
                .build();
        review.setId(1L);
        review.setCreated(LocalDateTime.now());
        review.setUpdated(LocalDateTime.now());


        var reviewPost = ReviewPost.builder()
                .id(200L)
                .post(post)
                .review(review)
                .build();
        reviewPost.setId(1L);

        // mocks
        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(postService.getPostByIdOrThrow(post.getId())).thenReturn(post);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewPostRepository.save(any(ReviewPost.class))).thenReturn(reviewPost);

        // when
        ReviewDTO result = reviewServiceImpl.createReviewPost(post.getId(), title, content);

        // then
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(content, result.getContent());
        assertEquals(mockUser.getUsername(), result.getUser().getUsername());

        verify(auth).getAuthenticatedUser();
        verify(postService).getPostByIdOrThrow(post.getId());
        verify(reviewRepository).save(any());
        verify(reviewPostRepository).save(any());
    }

    @Test
    void testCreateReviewList_success() {
        // given
        var list = ListMovie.builder()
                .title("Matrix")
                .build();

        String title = "Cool movie";
        String content = "I liked it a lot.";

        User mockUser = User.builder()
                .id(1L)
                .username("john_doe")
                .build();

        var review = Review.builder()
                .title(title)
                .content(content)
                .user(mockUser)
                .module(ModulesConstant.list)
                .idItem(list.getId())
                .reactions(List.of())
                .build();
        review.setId(1L);
        review.setCreated(LocalDateTime.now());
        review.setUpdated(LocalDateTime.now());


        var reviewList = ReviewList.builder()
                .id(200L)
                .movieList(list)
                .review(review)
                .build();
        reviewList.setId(1L);

        // mocks
        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(listService.getListByIdOrThrow(list.getId())).thenReturn(list);
        when(reviewRepository.save(any(Review.class))).thenReturn(review);
        when(reviewListRepository.save(any(ReviewList.class))).thenReturn(reviewList);

        // when
        ReviewDTO result = reviewServiceImpl.createReviewList(list.getId(), title, content);

        // then
        assertNotNull(result);
        assertEquals(title, result.getTitle());
        assertEquals(content, result.getContent());
        assertEquals(mockUser.getUsername(), result.getUser().getUsername());

        verify(auth).getAuthenticatedUser();
        verify(listService).getListByIdOrThrow(list.getId());
        verify(reviewRepository).save(any());
        verify(reviewListRepository).save(any());
    }


}
