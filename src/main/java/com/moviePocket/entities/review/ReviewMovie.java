/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.review;


import com.moviePocket.entities.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idMovie"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "idReview", nullable = false)
    private Review review;

    public ReviewMovie(Movie movie, Review movieReview) {
        this.movie = movie;
        this.review = movieReview;
    }
}
