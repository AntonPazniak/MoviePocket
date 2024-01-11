/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.review;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "like_review", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idReview"}))
@Getter
@Setter
@NoArgsConstructor
public class LikeMovieReview extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idReview")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean lickOrDis;

    public LikeMovieReview(Review movieReview, User user, boolean lickOrDis) {
        this.review = movieReview;
        this.user = user;
        this.lickOrDis = lickOrDis;
    }
}