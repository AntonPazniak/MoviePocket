package com.moviePocket.repository.image;

import com.moviePocket.entities.image.ImageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, Long> {


}
