/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.movie;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.google.gson.annotations.SerializedName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;


@Data
@Entity
@Table(name = "companies", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductionCompany {

    @Id
    @SerializedName("id")
    @Column(name = "id")
    private Long id;

    @SerializedName("logo_path")
    private String logoPath;

    @SerializedName("name")
    private String name;

    @SerializedName("origin_country")
    private String originCountry;

    @ManyToMany(mappedBy = "production_companies")
    @JsonBackReference
    private List<Movie> movies;


}
