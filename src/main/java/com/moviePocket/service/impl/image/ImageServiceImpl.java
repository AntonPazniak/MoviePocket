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
import java.awt.geom.Ellipse2D;
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
    public void handleFileUpload(MultipartFile file) {
        ImageEntity imageEntity = new ImageEntity();
        byte[] imageData = file.getBytes();
        String originalFilename = file.getOriginalFilename();
        imageEntity.setName(originalFilename);
        imageEntity.setData(imageData);
        imageRepository.save(imageEntity);
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

    public ImageEntity resizeImage(MultipartFile file) {
        try {

            if (file.getSize() > 1048576) {
                // You can handle the case where the file size is too large, for example, throw an exception or return null
                throw new IllegalArgumentException("File size exceeds the maximum allowed size (1 MB).");
            }
            BufferedImage originalImage = ImageIO.read(file.getInputStream());

            int standardWidth = 1000;
            int standardHeight = 1000;

            double scaleFactor = Math.min(1.0 * standardWidth / originalImage.getWidth(),
                    1.0 * standardHeight / originalImage.getHeight());

            int newWidth = (int) (originalImage.getWidth() * scaleFactor);
            int newHeight = (int) (originalImage.getHeight() * scaleFactor);

            int diameter = Math.min(newWidth, newHeight);
            int borderWidth = 5;

            BufferedImage bufferedImage = new BufferedImage(diameter, diameter, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = bufferedImage.createGraphics();

            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setClip(new Ellipse2D.Float(0, 0, diameter, diameter));
            g2d.drawImage(originalImage, 0, 0, diameter, diameter, null);

            g2d.setColor(Color.WHITE); // Set border color
            g2d.setStroke(new BasicStroke(borderWidth));
            g2d.draw(new Ellipse2D.Float(borderWidth / 2, borderWidth / 2, diameter - borderWidth, diameter - borderWidth));

            g2d.dispose();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, "png", byteArrayOutputStream);
            byte[] imageData = byteArrayOutputStream.toByteArray();

            ImageEntity imageEntity = new ImageEntity();
            imageEntity.setName("Avatar");
            imageEntity.setData(imageData);
            imageRepository.save(imageEntity);

            return imageEntity;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
