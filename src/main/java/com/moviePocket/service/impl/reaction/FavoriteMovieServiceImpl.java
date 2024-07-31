/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.impl.reaction;

import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.rating.FavoriteMovie;
import com.moviePocket.db.repository.rating.FavoriteMovieRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.reaction.ReactionMovie;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Qualifier("favoriteMovieService")
public class FavoriteMovieServiceImpl implements ReactionMovie {

    private final FavoriteMovieRepository favoriteMovieRepository;
    private final MovieServiceImpl movieService;
    private final AuthUser auth;

    @Override
    public void setOrDelete(Long idMovie) {
        Optional<FavoriteMovie> optionalFavoriteMovie = favoriteMovieRepository
                .findByUserAndMovie_Id(auth.getAuthenticatedUser(), idMovie);

        if (optionalFavoriteMovie.isPresent()) {
            favoriteMovieRepository.delete(optionalFavoriteMovie.get());
        } else {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            if (movie != null) {
                FavoriteMovie newFavoriteMovie = new FavoriteMovie(auth.getAuthenticatedUser(), movie);
                favoriteMovieRepository.save(newFavoriteMovie);
            }
        }
    }

    @Override
    public Boolean getReaction(Long idMovie) {
        return favoriteMovieRepository.existsByUserAndMovie_Id(auth.getAuthenticatedUser(), idMovie);
    }

    @Override
    public List<Movie> getAllMyReactions() {
        return favoriteMovieRepository.findAllMoviesByUser(auth.getAuthenticatedUser());
    }

    @Override
    public Integer getCountReactionByIdMovie(Long idMovie) {
        return favoriteMovieRepository.getAllCountByMovieId(idMovie);
    }

}
