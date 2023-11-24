package com.moviePocket.entities.user;

import com.moviePocket.entities.movie.list.ParsMovieList;
import com.moviePocket.entities.movie.rating.Rating;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsUserPage {

    private String username;
    private String bio;
    private Date created;
    private List<ParsMovieList> movieLL;
    private List<Long> likeMovie;
    private List<Long> dislikeMovie;
    private List<Long> watchedMovie;
    private List<Long> toWatchMovie;
    private List<Rating> ratingMovie;
}
