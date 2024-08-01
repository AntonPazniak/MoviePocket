/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.review;

import com.moviePocket.db.entities.BaseEntity;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "review_like", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idReview"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewLike extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idReview")
    private Review review;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean lickOrDis;
}
