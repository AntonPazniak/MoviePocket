/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.service.inter.image;

import com.moviePocket.db.entities.image.ImageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ResponseEntity<byte[]> getImageById(Long id);

    void deleteImage(Long id);

    void handleFileUpload(MultipartFile file);

    byte[] getImageDataById(Long id);

    ImageEntity resizeImage(MultipartFile file);
}
