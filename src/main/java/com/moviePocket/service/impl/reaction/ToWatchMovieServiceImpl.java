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
import com.moviePocket.db.entities.rating.ToWatchMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.rating.ToWatchMovieRepository;
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
@Qualifier("toWatchMovieService")
public class ToWatchMovieServiceImpl implements ReactionMovie {

    private final ToWatchMovieRepository toWatchMovieRepository;
    private final MovieServiceImpl movieService;
    private final AuthUser auth;

    @Override
    public void setOrDelete(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        Optional<ToWatchMovie> optionalToWatchMovie = toWatchMovieRepository
                .findByUserAndMovie_Id(user, idMovie);

        if (optionalToWatchMovie.isPresent()) {
            toWatchMovieRepository.delete(optionalToWatchMovie.get());
        } else {
            Movie movie = movieService.setMovieIfNotExist(idMovie);
            if (movie != null) {
                ToWatchMovie newToWatchMovie = new ToWatchMovie(user, movie);
                toWatchMovieRepository.save(newToWatchMovie);
            }
        }
    }

    @Override
    public Boolean getReaction(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        return toWatchMovieRepository.existsByUserAndMovie_Id(user, idMovie);
    }

    @Override
    public List<Movie> getAllMyReactions() {
        User user = auth.getAuthenticatedUser();
        return toWatchMovieRepository.findAllMoviesByUser(user);
    }

    @Override
    public Integer getCountReactionByIdMovie(Long idMovie) {
        return toWatchMovieRepository.getAllCountByMovieId(idMovie);
    }
}
