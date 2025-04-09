//package com.moviePocket.service.rating;
//
//import com.moviePocket.db.entities.movie.Movie;
//import com.moviePocket.db.entities.rating.DislikedMovie;
//import com.moviePocket.db.entities.user.User;
//import com.moviePocket.db.repository.reaction.DislikedMovieRepository;
//import com.moviePocket.impl.auth.AuthUser;
//import com.moviePocket.impl.movie.MovieServiceImpl;
//import com.moviePocket.impl.reaction.DislikedMovieServiceImpl;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.ActiveProfiles;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@SpringBootTest
//@ActiveProfiles("test")
//class DislikedMovieServiceImplTest {
//
//    @InjectMocks
//    private DislikedMovieServiceImpl dislikedMovieService;
//
//    @Mock
//    private DislikedMovieRepository dislikedMovieRepository;
//
//    @Mock
//    private MovieServiceImpl movieService;
//
//    @Mock
//    private AuthUser auth;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void testSetOrDeleteWhenMovieIsNotDisliked() {
//        Long movieId = 1L;
//        User user = mock(User.class); // Мокируем объект User
//        Movie movie = mock(Movie.class); // Мокируем объект Movie
//
//        when(auth.getAuthenticatedUser()).thenReturn(user);
//        when(movieService.setMovieIfNotExist(movieId)).thenReturn(movie);
//
//        dislikedMovieService.setOrDelete(movieId);
//
//        verify(dislikedMovieRepository).save(any(DislikedMovie.class));
//    }
//
//    @Test
//    void testSetOrDeleteWhenMovieIsDisliked() {
//        Long movieId = 1L;
//        User user = mock(User.class); // Мокируем объект User
//        Movie movie = mock(Movie.class); // Мокируем объект Movie
//        DislikedMovie dislikedMovie = new DislikedMovie(user, movie);
//
//        when(auth.getAuthenticatedUser()).thenReturn(user);
//        when(dislikedMovieRepository.getByUserAndMovie_Id(user, movieId))
//                .thenReturn(Optional.of(dislikedMovie));
//
//        dislikedMovieService.setOrDelete(movieId);
//
//        verify(dislikedMovieRepository).delete(dislikedMovie);
//    }
//
//    @Test
//    void testGetReaction() {
//        Long movieId = 1L;
//        User user = mock(User.class); // Мокируем объект User
//
//        when(auth.getAuthenticatedUser()).thenReturn(user);
//        when(dislikedMovieRepository.existsByUserAndMovie_Id(user, movieId))
//                .thenReturn(true);
//
//        Boolean result = dislikedMovieService.getReaction(movieId);
//
//        assertTrue(result);
//    }
//
//    @Test
//    void testGetAllMyReactions() {
//        User user = mock(User.class); // Мокируем объект User
//        Movie movie = mock(Movie.class); // Мокируем объект Movie
//        DislikedMovie dislikedMovie = new DislikedMovie(user, movie);
//
//        when(auth.getAuthenticatedUser()).thenReturn(user);
//        when(dislikedMovieRepository.findAllByUser_Id(user))
//                .thenReturn(List.of(movie));
//
//        List<Movie> reactions = dislikedMovieService.getAllMyReactions();
//
//        assertEquals(1, reactions.size());
//        assertEquals(movie, reactions.get(0));
//    }
//
//    @Test
//    void testGetCountReactionByIdMovie() {
//        Long movieId = 1L;
//        Integer count = 5;
//
//        when(dislikedMovieRepository.getAllCountByIdMovie(movieId))
//                .thenReturn(count);
//
//        Integer result = dislikedMovieService.getCountReactionByIdMovie(movieId);
//
//        assertEquals(count, result);
//    }
//}