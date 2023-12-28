package com.moviePocket.entities.user;

import com.moviePocket.controller.dto.UserPostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ParsBlockedUser {
    private UserPostDto user;
    private Date dateBlocked;
    private UserPostDto admin;
    private String comment;
}

