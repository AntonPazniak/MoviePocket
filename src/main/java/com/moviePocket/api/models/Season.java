package com.moviePocket.api.models;

import com.google.gson.annotations.SerializedName;

public class Season {
    @SerializedName("air_date")
    private String airDate;

    @SerializedName("episode_count")
    private int episodeCount;

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    @SerializedName("overview")
    private String overview;

    @SerializedName("poster_path")
    private String posterPath;

    @SerializedName("season_number")
    private int seasonNumber;

    @SerializedName("vote_average")
    private double voteAverage;
}
