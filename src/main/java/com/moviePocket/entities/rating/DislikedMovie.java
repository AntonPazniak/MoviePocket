/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.rating;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movie_disliked", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idMovie"}))
public class DislikedMovie extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

}