package com.moviePocket.controller.image;

import com.moviePocket.service.inter.image.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


//    @PostMapping("/upload")
//    public ResponseEntity<String> handleFileUpload(@RequestParam("file") MultipartFile file) {
//        return imageService.handleFileUpload(file);
//    }
}
