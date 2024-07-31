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
import com.moviePocket.db.entities.rating.Rating;
import com.moviePocket.db.entities.rating.RatingMovie;
import com.moviePocket.db.entities.user.User;
import com.moviePocket.db.repository.reaction.RatingMovieRepository;
import com.moviePocket.exception.NotFoundException;
import com.moviePocket.service.impl.auth.AuthUser;
import com.moviePocket.service.inter.movie.MovieService;
import com.moviePocket.service.inter.reaction.RatingMovieService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RatingMovieServiceImpl implements RatingMovieService {

    private final RatingMovieRepository ratingMovieRepository;
    private final MovieService movieService;
    private final AuthUser auth;


    @Override
    public void setRating(Long idMovie, int rating) {
        var user = auth.getAuthenticatedUser();
        Movie movie = movieService.setMovieIfNotExist(idMovie);

        var ratingMovie = ratingMovieRepository.findByUserAndMovie_id(user, idMovie);
        if (ratingMovie.isEmpty()) {
            ratingMovieRepository.save(
                    new RatingMovie(user, movie, rating)
            );
        } else {
            var updateRatingMovie = ratingMovie.get();
            updateRatingMovie.setRating(rating);
            ratingMovieRepository.save(updateRatingMovie);
        }
    }

    @Override
    public void removeRating(Long idMovie) {
        User user = auth.getAuthenticatedUser();
        var ratingMovie = ratingMovieRepository.findByUserAndMovie_id(user, idMovie);
        if (ratingMovie.isPresent()) {
            ratingMovieRepository.delete(ratingMovie.get());
        } else {
            throw new NotFoundException("Can't delete the rating, because rating is not found");
        }
    }

    @Override
    public Integer getMyRatingByIdMovie(Long idMovie) {
        User user = auth.getAuthenticatedUser();

        var ratingMovie = ratingMovieRepository.findByUserAndMovie_id(user, idMovie);

        return ratingMovie.map(RatingMovie::getRating).orElse(0);
    }

    @Override
    public List<Rating> getAllUserRating() {
        User user = auth.getAuthenticatedUser();
        List<RatingMovie> ratingMovieList = ratingMovieRepository.findAllByUser(user);

        return ratingMovieList.stream()
                .map(e -> Rating.builder()
                        .rating(e.getRating())
                        .movie(e.getMovie())
                        .build())
                .collect(Collectors.toList());
    }


    @Override
    public Float getRatingByIdMovie(Long idMovie) {
        var rating = ratingMovieRepository.getAverageRatingByMovieId(idMovie);
        if (rating.isPresent()) {
            BigDecimal bd = BigDecimal.valueOf(rating.get());
            BigDecimal roundedNumber = bd.setScale(1, RoundingMode.HALF_UP);
            return (float) roundedNumber.doubleValue();
        }
        return 0.0f;
    }

    @Override
    public Integer getRatingCountByIdMovie(Long idMovie) {
        return ratingMovieRepository.countAllByMovie_id(idMovie);
    }
}