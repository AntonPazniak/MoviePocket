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
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "countries", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductionCountry {

    @Id
    @SerializedName("iso_3166_1")
    @Column(name = "iso_3166_1")
    private String iso31661;


    @SerializedName("name")
    private String name;

    @ManyToMany(mappedBy = "production_countries")
    @JsonBackReference
    private List<Movie> movies;

}
