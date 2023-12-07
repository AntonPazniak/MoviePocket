package com.moviePocket.controller.user;

import com.moviePocket.entities.user.ParsUserPage;
import com.moviePocket.entities.user.User;
import com.moviePocket.service.inter.list.MovieListService;
import com.moviePocket.service.inter.rating.*;
import com.moviePocket.service.inter.raview.ReviewService;
import com.moviePocket.service.inter.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    public final UserService userService;
    public final MovieListService movieListService;
    public final ReviewService reviewService;
    public final FavoriteMovieService favoriteMovieService;
    public final DislikedMovieService dislikedMovieService;
    public final WatchedMovieService watchedMovieService;
    public final RatingMovieService ratingMovieService;
    private final ToWatchMovieService toWatchMovieService;

    @GetMapping("/getAut")
    public ResponseEntity<Void> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println((authentication.getName()));
        if (authentication.getName().equals("anonymousUser"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/getUsernameAut")
    public ResponseEntity<String> getUsernameAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.getName().equals("anonymousUser"))
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(userService.findUserByEmail(authentication.getName()).getUsername(), HttpStatus.OK);
    }

    @GetMapping("/{username}")
    public ResponseEntity<ParsUserPage> getUserByUsername(@PathVariable String username) {
        User user = userService.findUserByUsername(username);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        ParsUserPage parsUserPage = new ParsUserPage(
                user.getUsername(),
                user.getBio(),
                user.getCreated(),
                user.getAvatar().getId(),
                movieListService.getAllMyList(user.getEmail()).getBody(),
                favoriteMovieService.getAllUserFavoriteMovies(user.getEmail()).getBody(),
                dislikedMovieService.getAllUserDislikedMovie(user.getEmail()).getBody(),
                watchedMovieService.getAllUserWatched(user.getEmail()).getBody(),
                toWatchMovieService.getAllUserToWatch(user.getEmail()).getBody(),
                ratingMovieService.getAllUserRatingMovie(user.getEmail()).getBody()
        );
        return new ResponseEntity<>(parsUserPage, HttpStatus.OK);
    }
}