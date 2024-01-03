package com.moviePocket.entities.user;

import com.moviePocket.controller.dto.UserPostDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.crypto.Data;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BlockPars {
    private UserPostDto user;
    private Data dateBlacked;
    private UserPostDto admin;
    private String comment;
}
