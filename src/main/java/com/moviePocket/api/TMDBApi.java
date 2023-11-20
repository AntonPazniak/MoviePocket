package com.moviePocket.api;

import com.google.gson.Gson;
import com.moviePocket.api.models.Movie;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class TMDBApi {
    private static final String language = "us-US";

    public static Movie getInfoMovie(Long id) {
        if (id > 0)
            return getMovieDetails(id);
        else
            return getTVsInfo(Math.abs(id));
    }

    public static String getTrailerUrl(Long id) {
        if (id > 0)
            return getMovieTrailerUrl(id);
        else
            return getMovieTrailerUrl(Math.abs(id));
    }

    private static Movie getMovieDetails(Long movieId) {
        OkHttpClient client = new OkHttpClient();

        String url = "https://api.themoviedb.org/3/movie/" + movieId + "?language=" + language + "&api_key=1da35d58fd12497b111e4dd1c4a4c004";

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                String responseString = response.body().string();
                Gson gson = new Gson();
                return gson.fromJson(responseString, Movie.class);
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


    private static Movie getTVsInfo(Long idTV) {
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
                String responseString = response.body().string();
                Gson gson = new Gson();
                Movie movie = gson.fromJson(responseString, Movie.class);
                movie.setId(movie.getId() * (-1));
                return movie;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}
