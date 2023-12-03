package com.moviePocket.entities.list;

import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.Movie;
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
public class ParsList {

    private Long id;
    private String title;
    private String content;
    private List<Genre> genres;
    private List<Movie> movies;
    private int[] likeOrDis;
    private String username;
    private Date create;
    private Date update;

}
