package com.moviePocket.component;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.exception.NotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class TMDBClient {
    public MovieTMDB getMovieInfo(Long id) {
        var movie = TMDBApi.getInfoMovie(id);
        if (movie.isPresent())
            return movie.get();
        else
            throw new NotFoundException("Movie not found");
    }

    public Optional<Movie> getShortInfoMovie(Long id) {
        return TMDBApi.getShortInfoMovie(id);
    }
}
