package com.moviePocket.controller.android;

import com.moviePocket.service.inter.android.AndroidService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/android")
public class AndroidController {

    private final AndroidService androidService;

    @Autowired
    public AndroidController(AndroidService androidService) {
        this.androidService = androidService;
    }

    @GetMapping(path = "/")
    public ResponseEntity<String> existTracingByIdMovie(@RequestParam("url") String url) {
        return androidService.getData(url);
    }
}
