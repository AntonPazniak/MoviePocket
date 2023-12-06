package com.moviePocket.service.impl.image;

import com.moviePocket.entities.image.ImageEntity;
import com.moviePocket.repository.image.ImageRepository;
import com.moviePocket.service.inter.image.ImageService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;

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

    @SneakyThrows
    @Override
    public ImageEntity handleFileUpload(MultipartFile file) {
        ImageEntity imageEntity = new ImageEntity();
        byte[] imageData = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        imageEntity.setName(originalFilename);
        imageEntity.setData(imageData);
        imageRepository.save(imageEntity);
        return imageEntity;
    }

    @Override
    public byte[] getImageDataById(Long id) {
        return imageRepository.findById(id)
                .map(ImageEntity::getData)
                .orElse(null);
    }


    @SneakyThrows
    public ImageEntity createMoviePoster(List<String> posterUrls, String outputFilePath) {
        int numPosters = posterUrls.size();

        int width = 1000;
        int height = 1000;
        int rows;
        int cols;
        if (numPosters == 2) {
            rows = 1;
            cols = 2;
        } else if (numPosters == 4) {
            rows = 2;
            cols = 2;
        } else if (numPosters % 3 == 0) {
            rows = numPosters / 3;
            cols = 3;
        } else {
            rows = (numPosters / 3) + 1;
            cols = 2;
        }

        int posterWidth = width / cols;
        int posterHeight = height / rows;

        BufferedImage poster = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = poster.createGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, width, height);

        int i = 0;
        for (String imageUrl : posterUrls) {
            try {
                BufferedImage posterImage = ImageIO.read(new URL(imageUrl));

                double scaleFactor = Math.min(1.0 * posterWidth / posterImage.getWidth(),
                        1.0 * posterHeight / posterImage.getHeight());
                int newWidth = (int) (posterImage.getWidth() * scaleFactor);
                int newHeight = (int) (posterImage.getHeight() * scaleFactor);

                Image scaledPoster = posterImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);

                int x = i % cols * posterWidth + (posterWidth - newWidth) / 2;
                int y = i / cols * posterHeight + (posterHeight - newHeight) / 2;

                g2d.drawImage(scaledPoster, x, y, null);
                i++;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            ImageIO.write(poster, "jpg", new File(outputFilePath));
        } catch (IOException e) {
            e.printStackTrace();
        }

        g2d.dispose();

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(poster, "jpg", byteArrayOutputStream);
        byte[] imageData = byteArrayOutputStream.toByteArray();

        ImageEntity posterEntity = new ImageEntity();
        posterEntity.setName("MoviePoster");
        posterEntity.setData(imageData);
        imageRepository.save(posterEntity);

        return posterEntity;

    }

    public void delImage(ImageEntity image) {
        imageRepository.delete(image);
    }
}
