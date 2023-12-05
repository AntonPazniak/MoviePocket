package com.moviePocket.entities.movie;

import com.google.gson.annotations.SerializedName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "movie")
public class Movie {

    @Id
    @SerializedName("id")
    Long id;

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

}