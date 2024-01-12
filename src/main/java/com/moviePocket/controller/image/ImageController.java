/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.image;

import com.moviePocket.service.inter.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/images")
public class ImageController {

    @Autowired
    private ImageService imageService;

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getImageById(@PathVariable Long id) {
        return imageService.getImageById(id);
    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
//        imageService.deleteImage(id);
//        return ResponseEntity.noContent().build();
//    }


    @PostMapping("/upload")
    public ResponseEntity<Void> handleFileUpload(@RequestParam("file") MultipartFile file) {
        imageService.handleFileUpload(file);
        return (ResponseEntity<Void>) ResponseEntity.ok();
    }
}
