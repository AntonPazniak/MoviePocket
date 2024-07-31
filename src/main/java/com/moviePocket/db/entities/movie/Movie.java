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
import com.moviePocket.db.entities.list.ListMovie;
import com.moviePocket.db.entities.review.ReviewMovie;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;


@Entity(name = "movie")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Movie {

    @Id
    @SerializedName("id")
    private Long id;

    @SerializedName("title")
    private String title;

    @ManyToMany
    @JoinTable(
            name = "movie_genre",
            joinColumns = @JoinColumn(name = "id_movie"),
            inverseJoinColumns = @JoinColumn(name = "id_genre"))
    @SerializedName("genres")
    private List<Genre> genres;

    @ManyToMany
    @JoinTable(
            name = "movie_companies",
            joinColumns = @JoinColumn(name = "id_movie"),
            inverseJoinColumns = @JoinColumn(name = "id_company"))
    @SerializedName("production_companies")
    private List<ProductionCompany> production_companies;

    @ManyToMany
    @JoinTable(
            name = "movie_countries",
            joinColumns = @JoinColumn(name = "id_movie", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "id_iso_3166_1"))
    @SerializedName("production_countries")
    private List<ProductionCountry> production_countries;

    @SerializedName("poster_path")
    private String poster_path;

    @SerializedName("backdrop_path")
    private String backdrop_path;

    @SerializedName("release_date")
    private LocalDate release_date;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("imdb_id")
    private String imdb_id;

    @SerializedName("overview")
    @Column(columnDefinition = "TEXT")
    private String overview;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL)
    @JsonBackReference
    private List<ReviewMovie> reviews;

    @ManyToMany(mappedBy = "movies")
    @JsonBackReference
    private transient List<ListMovie> lists;

}
