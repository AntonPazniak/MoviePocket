package com.moviePocket.service.impl.movie;


import com.moviePocket.api.TMDBApi;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.repository.movie.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class GenreInitializer implements CommandLineRunner {

    @Autowired
    private GenreRepository genreRepository;

    @Override
    public void run(String... args) {
        long count = genreRepository.count();
        if (count < 19) {
            List<Genre> genres = TMDBApi.getGenres();
            assert genres != null;
            genreRepository.saveAll(genres);
        }
    }

}
