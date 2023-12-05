package com.moviePocket.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.moviePocket.api.models.MovieTMDB;
import com.moviePocket.entities.movie.Genre;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.movie.ProductionCompany;
import com.moviePocket.entities.movie.ProductionCountry;
import com.moviePocket.util.LocalDateAdapter;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class TMDBApi {
    private static final String language = "us-US";
    private static Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .create();

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

    public static String getTrailerUrl(Long id) {
        if (id > 0)
            return getMovieTrailerUrl(id);
        else
            return getMovieTrailerUrl(Math.abs(id));
    }

    private static String getMovieDetails(Long idMovie) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.themoviedb.org/3/movie/" + idMovie + "?language=" + language + "&api_key=1da35d58fd12497b111e4dd1c4a4c004";

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
            e.printStackTrace();
            return null;
        }
    }

    private static String getMovieTrailerUrl(Long movieId) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/movie/" + movieId + "/videos?language=" + language + "&api_key=1da35d58fd12497b111e4dd1c4a4c004";

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
            e.printStackTrace();
        }

        return null;
    }


    private static String getTVsInfo(Long idTV) {
        OkHttpClient client = new OkHttpClient();
        String url = "https://api.themoviedb.org/3/tv/" + idTV + "?language=" + language + "&api_key=1da35d58fd12497b111e4dd1c4a4c004";

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
            e.printStackTrace();
        }
        return null;
    }

    public static List<Genre> getGenres() {
        OkHttpClient client = new OkHttpClient();
        String url0 = "https://api.themoviedb.org/3/genre/movie/list?api_key=1da35d58fd12497b111e4dd1c4a4c004";
        String url1 = "https://api.themoviedb.org/3/genre/tv/list?api_key=1da35d58fd12497b111e4dd1c4a4c004";

        Set<Genre> genreSet = executeRequest(url0);
        List<Genre> genreList = new ArrayList<>(genreSet);
        genreSet = executeRequest(url1);
        genreList.addAll(genreSet);

        return genreList;
    }

    private static Set<Genre> executeRequest(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Set<Genre> genreSet = new HashSet<>();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
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
            e.printStackTrace();
        }

        return genreSet;
    }


    public static Movie parseTVSeries(String tvSeriesInfoJson) {
        Movie movie = null;
        try {
            JSONObject jsonObject = new JSONObject(tvSeriesInfoJson);
            Long id = jsonObject.getLong("id") * -1;
            String posterPath = jsonObject.optString("poster_path", null);
            String backdropPath = jsonObject.optString("backdrop_path", null);
            String title = jsonObject.getString("name");
            List<Genre> genres = gson.fromJson(jsonObject.getJSONArray("genres").toString(), new TypeToken<List<Genre>>() {
            }.getType());
            LocalDate releaseDate = gson.fromJson(jsonObject.getString("first_air_date"), LocalDate.class);
            List<ProductionCountry> productionCountries = gson.fromJson(jsonObject.getJSONArray("production_countries").toString(), new TypeToken<List<ProductionCountry>>() {
            }.getType());
            List<ProductionCompany> productionCompanies = gson.fromJson(jsonObject.getJSONArray("production_companies").toString(), new TypeToken<List<ProductionCompany>>() {
            }.getType());
            JSONArray episodeRunTimeArray = jsonObject.getJSONArray("episode_run_time");
            int runtime = episodeRunTimeArray.isEmpty() ? 0 : episodeRunTimeArray.getInt(0);
            String overview = jsonObject.getString("overview");

            movie = new Movie(id, title, genres, productionCompanies, productionCountries, posterPath, backdropPath, releaseDate, runtime, null, overview);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return movie;
    }


}
