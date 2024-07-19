/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.movie;

import com.moviePocket.api.TMDBApi;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.movie.ProductionCompany;
import com.moviePocket.entities.movie.ProductionCountry;
import com.moviePocket.repository.movie.MovieRepository;
import com.moviePocket.repository.movie.ProductionCompanyRepository;
import com.moviePocket.repository.movie.ProductionCountryRepository;
import com.moviePocket.service.inter.movie.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class MovieServiceImpl implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ProductionCompanyRepository productionCompanyRepository;

    @Autowired
    private ProductionCountryRepository productionCountryRepository;

    public Movie setMovieIfNotExist(Long idMovie) {
        if (movieRepository.existsById(idMovie)) {
            return movieRepository.findById(idMovie).orElseThrow();
        }

        Movie movie = TMDBApi.getShortInfoMovie(idMovie);
        if (movie == null) {
            throw new NoSuchElementException("Movie with ID " + idMovie + " not found in external API.");
        }

        saveProductionCompanies(movie.getProduction_companies());
        saveProductionCountries(movie.getProduction_countries());
        movieRepository.save(movie);
        return movie;
    }

    private void saveProductionCompanies(List<ProductionCompany> productionCompanies) {
        if (productionCompanies != null) {
            for (ProductionCompany productionCompany : productionCompanies) {
                if (!productionCompanyRepository.existsById(productionCompany.getId())) {
                    productionCompanyRepository.save(productionCompany);
                }
            }
        }
    }

    private void saveProductionCountries(List<ProductionCountry> productionCountries) {
        if (productionCountries != null) {
            for (ProductionCountry productionCountry : productionCountries) {
                if (!productionCountryRepository.existsByIso31661(productionCountry.getIso31661())) {
                    productionCountryRepository.save(productionCountry);
                }
            }
        }
    }
}
