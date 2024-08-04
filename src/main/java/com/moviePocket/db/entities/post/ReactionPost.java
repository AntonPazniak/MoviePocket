/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.post;

import com.moviePocket.db.entities.BaseEntity;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.*;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "like_post", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idPost"}))
public class ReactionPost extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idPost")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean reaction;
}
