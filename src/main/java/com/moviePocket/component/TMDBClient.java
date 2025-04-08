package com.moviePocket.component;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.api.models.MovieTMDB;
import org.springframework.stereotype.Component;

@Component
public class TMDBClient {
    public MovieTMDB getMovieInfo(Long id) {
        return TMDBApi.getInfoMovie(id);
    }
}
