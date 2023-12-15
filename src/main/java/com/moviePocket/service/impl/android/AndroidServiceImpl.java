package com.moviePocket.service.impl.android;

import com.moviePocket.service.inter.android.AndroidService;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AndroidServiceImpl implements AndroidService {

    @Value("${api.key}")
    private String apiKey;

    public ResponseEntity<String> get(String url) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url + apiKey)
                .get()
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                assert response.body() != null;
                return ResponseEntity.ok(response.body().string());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
