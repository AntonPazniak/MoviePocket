package com.moviePocket.config;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TMDBConfig {

    @Value("${tmdb.api_key}")
    private String apiKey;

    private static String API_KEY;

    @PostConstruct
    public void init() {
        API_KEY = this.apiKey;
    }

    public static String getApiKey() {
        return API_KEY;
    }
}
