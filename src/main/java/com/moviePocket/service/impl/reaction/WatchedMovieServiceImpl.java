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
import com.moviePocket.db.entities.rating.WatchedMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.WatchedMovieRepository;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.inter.reaction.ReactionMovie;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
@Qualifier("watchedMovieService")
public class WatchedMovieServiceImpl implements ReactionMovie {

    private final WatchedMovieRepository watchedMovieRepository;
    private final MovieServiceImpl movieService;
    private final AuthUser auth;

    @Override
    public void setOrDelete(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        Optional<WatchedMovie> optionalWatchedMovie = watchedMovieRepository
                .findByUserAndMovie_Id(user, idMovie);

        if (optionalWatchedMovie.isPresent()) {
            watchedMovieRepository.delete(optionalWatchedMovie.get());
        } else {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            if (movie != null) {
                WatchedMovie newWatchedMovie = new WatchedMovie(user, movie);
                watchedMovieRepository.save(newWatchedMovie);
            }
        }
    }

    @Override
    public Boolean getReaction(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        return watchedMovieRepository.existsByUserAndMovie_Id(user, idMovie);
    }

    @Override
    public List<Movie> getAllMyReactions() {
        User user = auth.getAuthenticatedUser();
        return watchedMovieRepository.findAllMoviesByUser(user);
    }

    @Override
    public Integer getCountReactionByIdMovie(Long idMovie) {
        return watchedMovieRepository.getAllCountByMovieId(idMovie);
    }

}
