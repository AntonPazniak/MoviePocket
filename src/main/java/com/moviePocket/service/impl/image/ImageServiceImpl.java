package com.moviePocket.service.impl.image;

import com.moviePocket.entities.image.ImageEntity;
import com.moviePocket.repository.image.ImageRepository;
import com.moviePocket.service.inter.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ImageServiceImpl implements ImageService {

    private final ImageRepository imageRepository;

    @Autowired
    public ImageServiceImpl(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    @Override
    public ResponseEntity<byte[]> getImageById(Long id) {
        byte[] imageData = getImageDataById(id);

        if (imageData != null && imageData.length > 0) {
            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imageData);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public void deleteImage(Long id) {
        imageRepository.deleteById(id);
    }

    @Override
    public ResponseEntity<String> handleFileUpload(MultipartFile file) {
        try {
            byte[] imageData = file.getBytes();
            String originalFilename = file.getOriginalFilename();

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setName(originalFilename);
            imageEntity.setData(imageData);
            imageRepository.save(imageEntity);

            return ResponseEntity.ok("File uploaded successfully: " + originalFilename);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload file: " + e.getMessage());
        }
    }

    @Override
    public byte[] getImageDataById(Long id) {
        return imageRepository.findById(id)
                .map(ImageEntity::getData)
                .orElse(null);
    }

}
