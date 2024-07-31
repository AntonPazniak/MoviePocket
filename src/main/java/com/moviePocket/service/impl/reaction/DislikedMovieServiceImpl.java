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
import com.moviePocket.db.entities.rating.DislikedMovie;
import com.moviePocket.db.repository.rating.DislikedMovieRepository;
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
@Qualifier("dislikedMovieService")
public class DislikedMovieServiceImpl implements ReactionMovie {

    private final DislikedMovieRepository dislikedMovieRepository;
    private final MovieServiceImpl movieService;
    private final AuthUser auth;


    @Override
    public void setOrDelete(Long idMovie) {
        Optional<DislikedMovie> optionalDislikedMovie = dislikedMovieRepository
                .getByUserAndMovie_Id(auth.getAuthenticatedUser(), idMovie);

        if (optionalDislikedMovie.isPresent()) {
            dislikedMovieRepository.delete(optionalDislikedMovie.get());
        } else {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            DislikedMovie newDislikedMovie = new DislikedMovie(auth.getAuthenticatedUser(), movie);
            dislikedMovieRepository.save(newDislikedMovie);
        }
    }

    @Override
    public Boolean getReaction(Long idMovie) {
        return dislikedMovieRepository.existsByUserAndMovie_Id(
                auth.getAuthenticatedUser(), idMovie);
    }

    @Override
    public List<Movie> getAllMyReactions() {
        return dislikedMovieRepository.findAllByUser_Id(auth.getAuthenticatedUser());
    }

    @Override
    public Integer getCountReactionByIdMovie(Long idMovie) {
        return dislikedMovieRepository.getAllCountByIdMovie(idMovie);
    }

}
