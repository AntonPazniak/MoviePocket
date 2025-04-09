package com.moviePocket.service.movie;


import com.moviePocket.component.TMDBClient;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.repository.movie.MovieRepository;
import com.moviePocket.db.repository.movie.ProductionCompanyRepository;
import com.moviePocket.db.repository.movie.ProductionCountryRepository;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MovieServiceImplTest {

    @Mock
    private MovieRepository movieRepository;
    @Mock
    private ProductionCompanyRepository productionCompanyRepository;
    @Mock
    private ProductionCountryRepository productionCountryRepository;
    @Mock
    private TMDBClient tmdbClient;

    @InjectMocks
    private MovieServiceImpl movieServiceImpl;


    @Test
    void tesGetOrSetMovieIfNotExistOrThrowNotFoundException_existInDB_success() {
        // given
        Long idMovie = 1L;
        Movie movie = Movie.builder().id(idMovie).build();

        // mocks
        when(movieRepository.findById(idMovie)).thenReturn(Optional.of(movie));

        // when
        Movie result = movieServiceImpl.getOrSetMovieIfNotExistOrThrowNotFoundException(idMovie);

        // then
        assertEquals(movie, result);

        verify(movieRepository).findById(idMovie);
    }

    @Test
    void tesGetOrSetMovieIfNotExistOrThrowNotFoundException_notExistInDB_success() {
        // given
        Long idMovie = 1L;
        Movie movie = Movie.builder().id(idMovie).build();

        // mocks
        when(movieRepository.findById(idMovie)).thenReturn(Optional.empty());
        when(tmdbClient.getShortInfoMovie(idMovie)).thenReturn(Optional.of(movie));

        // when
        Movie result = movieServiceImpl.getOrSetMovieIfNotExistOrThrowNotFoundException(idMovie);

        // then
        assertEquals(movie, result);

        verify(movieRepository).findById(idMovie);
        verify(tmdbClient).getShortInfoMovie(idMovie);
        verify(movieRepository).save(movie);

    }

    @Test
    void tesGetOrSetMovieIfNotExistOrThrowNotFoundException_notExistInDB_failed() {
        // given
        Long idMovie = 1L;

        // mocks
        when(movieRepository.findById(idMovie)).thenReturn(Optional.empty());
        when(tmdbClient.getShortInfoMovie(idMovie)).thenReturn(Optional.empty());

        // when & then
        assertThrows(NotFoundException.class, () ->
                movieServiceImpl.getOrSetMovieIfNotExistOrThrowNotFoundException(idMovie)
        );

        verify(movieRepository).findById(idMovie);
        verify(tmdbClient).getShortInfoMovie(idMovie);
        verify(movieRepository, never()).save(any());
    }


}
