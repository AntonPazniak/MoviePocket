package com.moviePocket.service.tracing;


import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.component.TMDBClient;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.tracking.Tracking;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.tracking.TrackingRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.impl.tracing.TracingServiceImpl;
import com.moviePocket.service.impl.user.EmailSenderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.moviePocket.util.BuildMailReleased.buildEmailReleased;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TracingServiceImplTest {

    @Mock
    private TrackingRepository trackingRepository;

    @Mock
    private MovieServiceImpl movieService;

    @Mock
    private EmailSenderService emailSenderService;

    @Mock
    private AuthUser auth;

    @Mock
    private TMDBClient tmdbClient;

    @InjectMocks
    private TracingServiceImpl tracingService;


    @Test
    void testGetByIdMovie_returnsTrue_whenTrackingExists() {
        // given
        Long movieId = 1L;
        User mockUser = new User();
        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.existsByUserAndMovie_Id(mockUser, movieId)).thenReturn(true);

        // when
        Boolean result = tracingService.getByIdMovie(movieId);

        // then
        assertTrue(result);
        verify(auth).getAuthenticatedUser();
        verify(trackingRepository).existsByUserAndMovie_Id(mockUser, movieId);
    }

    @Test
    void testGetByIdMovie_returnsFalse_whenTrackingDoesNotExist() {
        // given
        Long movieId = 2L;
        User mockUser = new User();
        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.existsByUserAndMovie_Id(mockUser, movieId)).thenReturn(false);

        // when
        Boolean result = tracingService.getByIdMovie(movieId);

        // then
        assertFalse(result);
        verify(auth).getAuthenticatedUser();
        verify(trackingRepository).existsByUserAndMovie_Id(mockUser, movieId);
    }


    @Test
    void testSetOrDel_trackingDoesNotExist_movieInFuture_shouldSaveTracking() {
        // given
        Long movieId = 1L;
        User mockUser = new User();
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setRelease_date(LocalDate.now().plusDays(5));

        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.findByUserAndMovie_Id(mockUser, movieId)).thenReturn(Optional.empty());
        when(movieService.getOrSetMovieIfNotExistOrThrowNotFoundException(movieId)).thenReturn(movie);

        // when
        tracingService.setOrDel(movieId);

        // then
        verify(trackingRepository).save(Mockito.<Tracking>any());
    }


    @Test
    void testSetOrDel_trackingDoesNotExist_movieAlreadyReleased_shouldIgnoreTracking() {
        // given
        Long movieId = 1L;
        User mockUser = new User();
        Movie movie = new Movie();
        movie.setId(movieId);
        movie.setRelease_date(LocalDate.now().plusDays(-5));

        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.findByUserAndMovie_Id(mockUser, movieId)).thenReturn(Optional.empty());
        when(movieService.getOrSetMovieIfNotExistOrThrowNotFoundException(movieId)).thenReturn(movie);

        // when
        tracingService.setOrDel(movieId);

        // then
        verify(trackingRepository, never()).save(any());
    }

    @Test
    void testSetOrDel_trackingExists_shouldDeleteTracking() {
        // given
        Long movieId = 3L;
        User mockUser = new User();
        Tracking existingTracking = new Tracking();

        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.findByUserAndMovie_Id(mockUser, movieId)).thenReturn(Optional.of(existingTracking));

        // when
        tracingService.setOrDel(movieId);

        // then
        verify(trackingRepository).delete(existingTracking);

    }

    @Test
    void testGetAllByUser_returnsMoviesList_Exist() {
        // given
        User mockUser = new User();
        Movie movie1 = Movie.builder().id(1L).build();
        Movie movie2 = Movie.builder().id(2L).build();

        Tracking tracking1 = Tracking.builder().movie(movie1).user(mockUser).build();
        Tracking tracking2 = Tracking.builder().movie(movie2).user(mockUser).build();

        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.findAllByUser(mockUser)).thenReturn(List.of(tracking1, tracking2));

        // when
        List<Movie> result = tracingService.getAllByUser();

        // then
        assertEquals(2, result.size());
        assertTrue(result.contains(movie1));
        assertTrue(result.contains(movie2));

        verify(auth).getAuthenticatedUser();
        verify(trackingRepository).findAllByUser(mockUser);
    }

    @Test
    void testGetAllByUser_returnsMoviesList_Empty() {
        // given
        User mockUser = new User();

        when(auth.getAuthenticatedUser()).thenReturn(mockUser);
        when(trackingRepository.findAllByUser(mockUser)).thenReturn(List.of());

        // when
        List<Movie> result = tracingService.getAllByUser();

        // then
        assertEquals(0, result.size());

        verify(auth).getAuthenticatedUser();
        verify(trackingRepository).findAllByUser(mockUser);
    }

    @Test
    void testGetCountByIdMovie_returnsCountUser() {
        // given
        Long movieId = 5L;
        Long countUsers = 10L;
        when(trackingRepository.countAllByMovie_id(movieId)).thenReturn(countUsers);

        // when
        Long result = tracingService.getCountByIdMovie(movieId);

        // then
        assertEquals(countUsers, result);
    }

    @Test
    void testSendDailyMessages_successfulFlow() throws Exception {
        // given
        LocalDate tomorrow = LocalDate.now().plusDays(1);

        // mock user and movie
        User mockUser = new User();
        mockUser.setUsername("john_doe");
        mockUser.setEmail("john@example.com");

        Movie movie = Movie.builder().id(1L).build();

        Tracking tracking = Tracking.builder()
                .user(mockUser)
                .movie(movie)
                .dateRelease(tomorrow)
                .build();

        MovieTMDB movieTMDB = new MovieTMDB();
        movieTMDB.setTitle("Inception");
        movieTMDB.setOverview("A mind-bending thriller");

        when(trackingRepository.findByDateRelease(tomorrow)).thenReturn(List.of(tracking));
        when(tmdbClient.getMovieInfo(1L)).thenReturn(movieTMDB);

        // when
        tracingService.sendDailyMessages();

        // then
        verify(trackingRepository).findByDateRelease(tomorrow);
        verify(tmdbClient).getMovieInfo(1L);
        verify(emailSenderService).sendMailWithAttachment(
                mockUser.getEmail(),
                buildEmailReleased(
                        mockUser.getUsername(),
                        movieTMDB.getTitle(),
                        movieTMDB.getOverview(),
                        "https://moviepocket.projektstudencki.pl/film/" + movie.getId())
                , // subject text
                "Movie release tomorrow " + movieTMDB.getTitle()
        );
        verify(trackingRepository).deleteAll(List.of(tracking));
    }

}
