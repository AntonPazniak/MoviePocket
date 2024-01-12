/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.api.models;

import com.google.gson.annotations.SerializedName;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.ProductionCompany;
import com.moviePocket.entities.movie.ProductionCountry;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class MovieTMDB implements Serializable {
    private final String BASE_URL = "https://image.tmdb.org/t/p/w500";
    private final String STANDARD_POSTER_URL = "https://github.com/prymakD/MoviePocket/raw/16f04a6063f407cec8ee8eab29a4bd25c4ae111b/src/main/frontend/src/images/person.png";

    @SerializedName("adult")
    private boolean adult;

    @SerializedName("backdrop_path")
    private String backdropPath;

    @SerializedName("belongs_to_collection")
    private Object belongsToCollection;

    @SerializedName("budget")
    private int budget;

    @SerializedName("genres")
    private List<Genre> genres;

    @SerializedName("homepage")
    private String homepage;

    @SerializedName("id")
    private int id;

    @SerializedName("imdb_id")
    private String imdbId;

    @SerializedName("original_language")
    private String originalLanguage;

    @SerializedName("original_title")
    private String originalTitle;

    @SerializedName("overview")
    private String overview;

    @SerializedName("popularity")
    private double popularity;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("production_companies")
    private List<ProductionCompany> productionCompanies;

    @SerializedName("production_countries")
    private List<ProductionCountry> productionCountries;

    @SerializedName("release_date")
    private LocalDate releaseDate;

    @SerializedName("revenue")
    private int revenue;

    @SerializedName("runtime")
    private int runtime;

    @SerializedName("spoken_languages")
    private List<SpokenLanguage> spokenLanguages;

    @SerializedName("status")
    private String status;

    @SerializedName("tagline")
    private String tagline;

    @SerializedName("title")
    private String title;

    @SerializedName("video")
    private boolean video;

    @SerializedName("vote_average")
    private double voteAverage;

    @SerializedName("vote_count")
    private int voteCount;

    @SerializedName("created_by")
    private List<Object> createdBy;

    @SerializedName("episode_run_time")
    private List<Integer> episodeRunTime;

    @SerializedName("first_air_date")
    private LocalDate firstAirDate;

    @SerializedName("in_production")
    private boolean inProduction;

    @SerializedName("languages")
    private List<String> languages;

    @SerializedName("last_air_date")
    private String lastAirDate;

    @SerializedName("last_episode_to_air")
    private Episode lastEpisodeToAir;

    @SerializedName("name")
    private String name;

    @SerializedName("next_episode_to_air")
    private Object nextEpisodeToAir;

    @SerializedName("networks")
    private List<ProductionCompany> networks;

    @SerializedName("number_of_episodes")
    private int numberOfEpisodes;

    @SerializedName("number_of_seasons")
    private int numberOfSeasons;

    @SerializedName("origin_country")
    private List<String> originCountry;

    @SerializedName("original_name")
    private String originalName;

    @Getter
    @SerializedName("seasons")
    private List<Season> seasons;

    @Getter
    @SerializedName("type")
    private String type;

    public String getPosterPath() {
        if (posterPath != null)
            return BASE_URL + posterPath;
        else
            return STANDARD_POSTER_URL;
    }

    public String getTitle() {
        if (id > 0)
            return title;
        else
            return name;
    }

    public LocalDate getReleaseDate() {
        if (id > 0)
            return releaseDate;
        else
            return firstAirDate;
    }
}
