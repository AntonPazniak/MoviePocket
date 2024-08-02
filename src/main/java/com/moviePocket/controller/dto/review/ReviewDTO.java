/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.controller.dto.review;

import com.moviePocket.controller.dto.UserPostDto;
import com.moviePocket.db.entities.review.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDTO {

    private String title;
    private String content;
    private UserPostDto user;
    private LocalDateTime dataCreated;
    private LocalDateTime dataUpdated;
    private Long id;
    private ReactionDTO reactions;


    public static ReviewDTO parsReview(@NotNull Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .title(review.getTitle())
                .content(review.getContent())
                .dataCreated(review.getCreated())
                .dataUpdated(review.getUpdated())
                .user(UserPostDto.builder()
                        .username(review.getUser().getUsername())
                        .avatar(review.getUser().getAvatar() != null ? review.getUser().getAvatar().getId() : null)
                        .build())
                .build();
    }


}
