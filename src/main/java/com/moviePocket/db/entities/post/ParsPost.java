/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.post;

import com.moviePocket.controller.dto.UserPostDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ParsPost {
    private Long id;
    private String title;
    private String content;
    private int[] likeOrDis;
    private UserPostDto user;
    private LocalDateTime create;
    private LocalDateTime update;
    private Long idMovie;
    private Long idPerson;
    private Long idList;


    public ParsPost(Long id, String title, String content, int[] likeOrDis, UserPostDto user, LocalDateTime create, LocalDateTime update) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likeOrDis = likeOrDis;
        this.user = user;
        this.create = create;
        this.update = update;
    }
}
