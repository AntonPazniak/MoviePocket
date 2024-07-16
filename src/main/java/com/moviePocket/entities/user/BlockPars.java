/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.entities.user;

import com.moviePocket.controller.dto.UserPostDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@lombok.Data
@AllArgsConstructor
@NoArgsConstructor
public class BlockPars {
    private UserPostDto user;
    private LocalDateTime dateBlacked;
    private UserPostDto admin;
    private String comment;
}
