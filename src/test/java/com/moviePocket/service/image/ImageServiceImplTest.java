package com.moviePocket.service.image;

import com.moviePocket.entities.image.ImageEntity;
import com.moviePocket.repository.image.ImageRepository;
import com.moviePocket.service.impl.image.ImageServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ImageServiceImplTest {

    @Mock
    private ImageRepository imageRepository;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    void testGetImageById_ImageFound() {
        byte[] imageData = "Test Image Data".getBytes();
        when(imageRepository.findById(1L)).thenReturn(Optional.of(new ImageEntity("TestImage", imageData)));

        ResponseEntity<byte[]> response = imageService.getImageById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertArrayEquals(imageData, response.getBody());
        assertEquals(MediaType.IMAGE_JPEG, response.getHeaders().getContentType());
    }

    @Test
    void testGetImageById_ImageNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseEntity<byte[]> response = imageService.getImageById(1L);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteImage() {
        imageService.deleteImage(1L);

        Mockito.verify(imageRepository, Mockito.times(1)).deleteById(1L);
    }

    @Test
    void testHandleFileUpload() throws IOException {
        MultipartFile file = new MockMultipartFile("test.jpg", "test.jpg", "image/jpeg", "Test Image Data".getBytes());

        assertDoesNotThrow(() -> imageService.handleFileUpload(file));
    }

    @Test
    void testGetImageDataById_ImageFound() {
        byte[] imageData = "Test Image Data".getBytes();
        when(imageRepository.findById(1L)).thenReturn(Optional.of(new ImageEntity("TestImage", imageData)));

        byte[] result = imageService.getImageDataById(1L);

        assertNotNull(result);
        assertArrayEquals(imageData, result);
    }

    @Test
    void testGetImageDataById_ImageNotFound() {
        when(imageRepository.findById(1L)).thenReturn(Optional.empty());

        byte[] result = imageService.getImageDataById(1L);

        assertNull(result);
    }

    @Test
    void testCreateMoviePoster() {
        List<String> posterUrls = new ArrayList<>();
        posterUrls.add("https://example.com/poster1.jpg");
        posterUrls.add("https://example.com/poster2.jpg");

        ImageEntity result = imageService.createMoviePoster(posterUrls, "outputPoster.jpg");

        assertNotNull(result);
    }

    @Test
    void testDelImage() {
        ImageEntity imageEntity = new ImageEntity("TestImage", "Test Image Data".getBytes());
        when(imageRepository.findById(1L)).thenReturn(Optional.of(imageEntity));

        imageService.delImage(imageEntity);

        Mockito.verify(imageRepository, Mockito.times(1)).delete(imageEntity);
    }

}

