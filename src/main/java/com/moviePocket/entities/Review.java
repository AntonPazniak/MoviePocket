package com.moviePocket.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Review {

    private String title;
    private String content;
    private String username;
    private Date dataCreated;
    private Long idMovie;
    private Long id;

}
