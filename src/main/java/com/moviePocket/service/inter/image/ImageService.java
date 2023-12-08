package com.moviePocket.service.inter.image;

import com.moviePocket.entities.image.ImageEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ResponseEntity<byte[]> getImageById(Long id);

    void deleteImage(Long id);

    void handleFileUpload(MultipartFile file);

    byte[] getImageDataById(Long id);

    ImageEntity resizeImage(MultipartFile file);
}
