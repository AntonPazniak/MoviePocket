package com.moviePocket.service.impl.android;

import com.moviePocket.service.inter.android.AndroidService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class AndroidServiceImpl implements AndroidService {

    @Value("${api.key}")
    private String apiKey;

    public ResponseEntity<String> get(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);

        // Добавление API-ключа в URL (предполагается, что apiKey - это поле класса)
        url += apiKey;

        HttpEntity<String> requestEntity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        return restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                String.class
        );
    }
}
