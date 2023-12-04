package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.Rating;
import com.moviePocket.entities.rating.RatingMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.rating.RatingMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.rating.RatingMovieService;
import com.moviePocket.service.rating.WatchedMovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class RatingMovieServiceImpl implements RatingMovieService {
    @Autowired
    private RatingMovieRepository ratingMovieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieServiceImpl movieService;
    @Autowired
    private WatchedMovieService watchedMovieService;

    @Transactional
    public ResponseEntity<Void> setNewRatingMovie(String email, Long idMovie, int rating) {
        if (rating > 0 && rating < 11) {
            User user = userRepository.findByEmail(email);
            if (user == null)
                return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
            Movie movie = movieService.setMovie(idMovie);
            if (movie != null) {
                RatingMovie ratingMovie = ratingMovieRepository.findByUserAndMovie_id(user, idMovie);
                if (ratingMovie == null) {
                    ratingMovie = new RatingMovie(user, movie, rating);
                    watchedMovieService.setOrDeleteNewWatched(email, idMovie);
                } else {
                    ratingMovie.setRating(rating);
                }
                ratingMovieRepository.save(ratingMovie);
                return new ResponseEntity<>(HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @Transactional
    public ResponseEntity<Void> removeFromRatingMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        RatingMovie ratingMovie = ratingMovieRepository.findByUserAndMovie_id(user, idMovie);
        if (ratingMovie == null)
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        ratingMovieRepository.delete(ratingMovie);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Integer> getFromRatingMovie(String email, Long idMovie) {
        RatingMovie ratingMovie = ratingMovieRepository.findByUserAndMovie_id(
                userRepository.findByEmail(email), idMovie);
        if (ratingMovie == null)
            return ResponseEntity.ok(0);
        return ResponseEntity.ok(ratingMovie.getRating());
    }


    public ResponseEntity<List<Rating>> getAllUserRatingMovie(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<RatingMovie> ratingMovieList = ratingMovieRepository.findAllByUser(user);
        if (ratingMovieList.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(parsRatingMovieList(ratingMovieList));
    }

    private List<Rating> parsRatingMovieList(List<RatingMovie> ratingMovieList) {
        List<Rating> ratingList = new ArrayList<>();
        for (RatingMovie ratingMovie : ratingMovieList) {
            ratingList.add(new Rating(

                    ratingMovie.getRating(),
                    ratingMovie.getMovie()
            ));
        }
        return ratingList;
    }

    public ResponseEntity<Double> getMovieRating(Long idMovie) {
        Double rating = ratingMovieRepository.getAverageRatingByMovieId(idMovie);
        if (rating != null) {
            BigDecimal bd = BigDecimal.valueOf(rating);
            BigDecimal roundedNumber = bd.setScale(1, RoundingMode.HALF_UP);
            return ResponseEntity.ok(roundedNumber.doubleValue());
        }
        return ResponseEntity.ok(0.0);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        return ResponseEntity.ok(ratingMovieRepository.countAllByMovie_id(idMovie));
    }

}