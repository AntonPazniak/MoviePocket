/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.config.TMDBConfig;
import com.moviePocket.db.entities.movie.Genre;
import com.moviePocket.db.entities.movie.Movie;
import com.moviePocket.db.entities.movie.ProductionCompany;
import com.moviePocket.db.entities.movie.ProductionCountry;
import com.moviePocket.util.LocalDateAdapter;
import com.moviePocket.util.LocalDateTimeAdapter;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Slf4j
public class TMDBApi {

    private static final String language = "us-US";

    private static final Gson gson;

    static {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                .create();
    }

    public static MovieTMDB getInfoMovie(Long idMovie) {
        if (idMovie > 0) {
            String responseString = getMovieDetails(idMovie);
            if (responseString != null)
                return gson.fromJson(responseString, MovieTMDB.class);
        } else {
            String responseString = getTVsInfo(Math.abs(idMovie));
            if (responseString != null) {
                MovieTMDB movie = gson.fromJson(responseString, MovieTMDB.class);
                movie.setId(movie.getId() * (-1));
                return movie;
            }
        }
        return null;
    }

    public static Movie getShortInfoMovie(Long idMovie) {
        if (idMovie > 0) {
            String responseString = getMovieDetails(idMovie);
            if (responseString != null)
                return gson.fromJson(responseString, Movie.class);
        } else {
            String responseString = getTVsInfo(Math.abs(idMovie));
            if (responseString != null) {
                return parseTVSeries(responseString);
            }
        }
        return null;
    }

    private static String getMovieDetails(Long idMovie) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.themoviedb.org/3/movie/" + idMovie + "?language=" + language + "&api_key=" + TMDBConfig.getApiKey();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            } else
                return null;
        } catch (IOException e) {
            log.error(e.toString());
            return null;
        }
    }

    private static String getMovieTrailerUrl(Long movieId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?language=" + language + "&api_key=" + TMDBConfig.getApiKey();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray resultsArray = jsonObject.getJSONArray("results");
                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject videoObject = resultsArray.getJSONObject(i);
                    String type = videoObject.getString("type");
                    if ("Trailer".equals(type)) {
                        String key = videoObject.getString("key");
                        return "https://www.youtube.com/embed/" + key;
                    }
                }
            }
        } catch (IOException | JSONException e) {
            log.error(e.toString());
        }

        return null;
    }


    private static String getTVsInfo(Long idTV) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/tv/" + idTV + "?language=" + language + "&api_key=" + TMDBConfig.getApiKey();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return response.body().string();
            }
        } catch (IOException e) {
            log.error(e.toString());
        }
        return null;
    }

    public static List<Genre> getGenres() {
        OkHttpClient client = new OkHttpClient();
        String url0 = "https://api.themoviedb.org/3/genre/movie/list?api_key=" + TMDBConfig.getApiKey();
        ;
        String url1 = "https://api.themoviedb.org/3/genre/tv/list?api_key=" + TMDBConfig.getApiKey();
        ;

        Set<Genre> genreSet = executeRequest(client, url0);
        List<Genre> genreList = new ArrayList<>(genreSet);
        genreSet = executeRequest(client, url1);
        genreList.addAll(genreSet);

        return genreList;
    }

    private static Set<Genre> executeRequest(OkHttpClient client, String url) {
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Set<Genre> genreSet = new HashSet<>();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful() && response.body() != null) {
                String responseBody = response.body().string();
                JSONObject jsonObject = new JSONObject(responseBody);
                JSONArray resultsArray = jsonObject.getJSONArray("genres");

                for (int i = 0; i < resultsArray.length(); i++) {
                    JSONObject genreObject = resultsArray.getJSONObject(i);
                    Genre genre = gson.fromJson(genreObject.toString(), Genre.class);
                    genreSet.add(genre);
                }
            }
        } catch (IOException e) {
            log.error(e.toString());
        }

        return genreSet;
    }


    public static Movie parseTVSeries(String tvSeriesInfoJson) {
        try {
            JSONObject jsonObject = new JSONObject(tvSeriesInfoJson);
            JSONArray episodeRunTimeArray = jsonObject.getJSONArray("episode_run_time");

            return Movie.builder()
                    .id(jsonObject.getLong("id") * -1)
                    .title(jsonObject.getString("name"))
                    .genres(gson.fromJson(jsonObject.getJSONArray("genres").toString(), new TypeToken<List<Genre>>() {
                    }.getType()))
                    .production_companies(gson.fromJson(jsonObject.getJSONArray("production_companies").toString(), new TypeToken<List<ProductionCompany>>() {
                    }.getType()))
                    .production_countries(gson.fromJson(jsonObject.getJSONArray("production_countries").toString(), new TypeToken<List<ProductionCountry>>() {
                    }.getType()))
                    .poster_path(jsonObject.optString("poster_path", null))
                    .backdrop_path(jsonObject.optString("backdrop_path", null))
                    .release_date(gson.fromJson(jsonObject.getString("first_air_date"), LocalDate.class))
                    .runtime(episodeRunTimeArray.isEmpty() ? 0 : episodeRunTimeArray.getInt(0))
                    .overview(jsonObject.getString("overview"))
                    .build();
        } catch (JSONException e) {
            log.error(e.toString());
        }
        throw new NotFoundException("TV series not found");
    }


}
