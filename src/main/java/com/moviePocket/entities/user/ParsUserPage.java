package com.moviePocket.entities.user;

import com.moviePocket.entities.list.ParsList;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.rating.Rating;
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
    private List<ParsList> movieLL;
    private List<Movie> likeMovie;
    private List<Long> dislikeMovie;
    private List<Movie> watchedMovie;
    private List<Movie> toWatchMovie;
    private List<Rating> ratingMovie;
}
