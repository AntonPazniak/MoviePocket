package com.moviePocket.service.impl.android;

import com.fasterxml.jackson.databind.JsonNode;
import com.moviePocket.service.inter.android.AndroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidServiceImpl implements AndroidService {

    private final String apiKey;

    @Autowired
    public AndroidServiceImpl(@Value("${api.key}") String apiKey) {
        this.apiKey = apiKey;
    }

    public ResponseEntity<JsonNode> getData(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Добавление API-ключа в URL
        url += apiKey;

        System.out.println(url);

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                JsonNode.class
        );
    }
}
