/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.tracking;

import com.moviePocket.db.entities.BaseEntity;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "movie_tracking", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idMovie"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Tracking extends BaseEntity {

    private LocalDate dateRelease;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;


}
