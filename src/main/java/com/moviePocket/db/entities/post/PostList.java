/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.post;

import com.moviePocket.db.entities.list.ListMovie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "post_list", uniqueConstraints = @UniqueConstraint(columnNames = {"idPost", "idList"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idList", nullable = false)
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;


}