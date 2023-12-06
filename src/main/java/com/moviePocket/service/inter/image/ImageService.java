package com.moviePocket.service.inter.image;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {

    ResponseEntity<byte[]> getImageById(Long id);

    void deleteImage(Long id);

    ResponseEntity<String> handleFileUpload(MultipartFile file);

    byte[] getImageDataById(Long id);
}
