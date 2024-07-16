/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "genre", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Genre {

    @Id
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    @JsonBackReference
    private List<Movie> movies;

}
