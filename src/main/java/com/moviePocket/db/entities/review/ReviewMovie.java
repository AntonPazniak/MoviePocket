/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.review;


import com.moviePocket.db.entities.movie.Movie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idMovie"}))
@Data
@Builder
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

}
