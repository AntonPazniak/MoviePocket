/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.movie;

import com.moviePocket.component.TMDBClient;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.movie.ProductionCompany;
import com.moviePocket.db.entities.movie.ProductionCountry;
import com.moviePocket.db.repository.movie.MovieRepository;
import com.moviePocket.db.repository.movie.ProductionCompanyRepository;
import com.moviePocket.db.repository.movie.ProductionCountryRepository;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.inter.movie.MovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    private final MovieRepository movieRepository;
    private final ProductionCompanyRepository productionCompanyRepository;
    private final ProductionCountryRepository productionCountryRepository;
    private final TMDBClient tmdbClient;

    public Movie getOrSetMovieIfNotExistOrThrowNotFoundException(Long idMovie) {
        return movieRepository.findById(idMovie)
                .orElseGet(() -> {
                    Movie movie = tmdbClient.getShortInfoMovie(idMovie)
                            .orElseThrow(() -> new NotFoundException("Movie not found"));

                    saveProductionCompanies(movie.getProduction_companies());
                    saveProductionCountries(movie.getProduction_countries());

                    movieRepository.save(movie);
                    return movie;
                });
    }


    private void saveProductionCompanies(List<ProductionCompany> productionCompanies) {
        if (productionCompanies != null && !productionCompanies.isEmpty()) {
            List<Long> existingIds = productionCompanyRepository.findAllById(
                    productionCompanies.stream().map(ProductionCompany::getId).collect(Collectors.toList())
            ).stream().map(ProductionCompany::getId).toList();
            List<ProductionCompany> companiesToSave = productionCompanies.stream()
                    .filter(company -> !existingIds.contains(company.getId()))
                    .collect(Collectors.toList());
            if (!companiesToSave.isEmpty()) {
                productionCompanyRepository.saveAll(companiesToSave);
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
