package com.moviePocket.entities.movie.post;

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
public class ParsPost {
    private Long id;
    private String title;
    private String content;
    private List<Long> idMovieList;
    private int[] likeOrDis;
    private String username;
    private Date create;
    private Date update;
}
