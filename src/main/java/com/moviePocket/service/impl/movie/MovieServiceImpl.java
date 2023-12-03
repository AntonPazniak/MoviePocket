package com.moviePocket.service.impl.movie;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.movie.ProductionCompany;
import com.moviePocket.entities.movie.ProductionCountry;
import com.moviePocket.repository.movie.GenreRepository;
import com.moviePocket.repository.movie.MovieRepository;
import com.moviePocket.repository.movie.ProductionCompanyRepository;
import com.moviePocket.repository.movie.ProductionCountryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MovieServiceImpl {

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private GenreRepository genreRepository;
    @Autowired
    private ProductionCompanyRepository productionCompanyRepository;
    @Autowired
    private ProductionCountryRepository productionCountryRepository;

    public Boolean setMovie(Long idMovie) {
        if (!movieRepository.existsById(idMovie)) {
            Movie movie = TMDBApi.getShortInfoMovie(idMovie);
            if (movie != null) {
                if (movie.getProduction_companies() != null) {
                    for (ProductionCompany productionCompany : movie.getProduction_companies()) {
                        if (!productionCompanyRepository.existsById(productionCompany.getId())) {
                            productionCompanyRepository.save(productionCompany);
                        }
                    }
                }
                if (movie.getProduction_countries() != null) {
                    for (ProductionCountry productionCountry : movie.getProduction_countries()) {
                        if (!productionCountryRepository.existsByIso31661(productionCountry.getIso31661())) {
                            productionCountryRepository.save(productionCountry);
                        }
                    }
                }
                movieRepository.save(movie);
            }

        } else {
            return true;
        }


        return false;
    }

}
