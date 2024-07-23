/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.list;

import com.moviePocket.db.entities.BaseEntity;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "like_list", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idList"}))
public class LikeList extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idList")
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean lickOrDis;
}
