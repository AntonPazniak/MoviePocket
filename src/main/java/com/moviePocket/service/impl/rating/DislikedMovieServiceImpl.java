package com.moviePocket.service.impl.rating;

import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.DislikedMovie;
import com.moviePocket.entities.user.User;
import com.moviePocket.repository.rating.DislikedMovieRepository;
import com.moviePocket.repository.user.UserRepository;
import com.moviePocket.service.impl.movie.MovieServiceImpl;
import com.moviePocket.service.rating.DislikedMovieService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DislikedMovieServiceImpl implements DislikedMovieService {

    private final DislikedMovieRepository dislikedMovieRepository;
    private final UserRepository userRepository;
    private final MovieServiceImpl movieService;

    @Transactional
    public ResponseEntity<Void> setOrDeleteDislikedMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        DislikedMovie dislikedMovie = dislikedMovieRepository.findByUserAndMovie_Id(user, idMovie);
        Movie movie = movieService.setMovie(idMovie);
        if (movie != null) {
            if (dislikedMovie == null) {
                dislikedMovieRepository.save(
                        new DislikedMovie(userRepository.findByEmail(email), movie));
            } else {
                dislikedMovieRepository.delete(dislikedMovie);
            }
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    public ResponseEntity<Boolean> getFromDislikedMovie(String email, Long idMovie) {
        User user = userRepository.findByEmail(email);
        if (user == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        DislikedMovie dislikedMovie = dislikedMovieRepository.findByUserAndMovie_Id(
                user, idMovie);
        return ResponseEntity.ok(dislikedMovie != null);
    }

    public ResponseEntity<List<Movie>> getAllUserDislikedMovie(String email) {
        List<DislikedMovie> favoriteMoviesList = dislikedMovieRepository.findAllByUser(
                userRepository.findByEmail(email));
        List<Movie> movies = new ArrayList<>();
        for (DislikedMovie dislikedMovie : favoriteMoviesList) {
            movies.add(dislikedMovie.getMovie());
        }
        if (movies.isEmpty())
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        return ResponseEntity.ok(movies);
    }

    public ResponseEntity<Integer> getAllCountByIdMovie(Long idMovie) {
        int count = dislikedMovieRepository.getAllCountByIdMovie(idMovie);
        return ResponseEntity.ok(count);
    }
}
