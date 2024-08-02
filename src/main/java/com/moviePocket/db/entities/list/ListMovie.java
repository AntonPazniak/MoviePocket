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
import com.moviePocket.db.entities.image.ImageEntity;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.review.ReviewList;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "idUser"}))
public class ListMovie extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "list_item",
            joinColumns = @JoinColumn(name = "id_list"),
            inverseJoinColumns = @JoinColumn(name = "id_movie"))
    private List<Movie> movies;

    @OneToOne
    @JoinColumn(name = "idImage", referencedColumnName = "id")
    private ImageEntity poster;

    @OneToMany(mappedBy = "movieList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListGenres> listGenres;

    @OneToMany(mappedBy = "movieList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewList> reviews;

}
