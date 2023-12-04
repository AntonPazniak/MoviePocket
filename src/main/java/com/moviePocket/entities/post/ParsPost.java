package com.moviePocket.entities.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParsPost {
    private Long id;
    private String title;
    private String content;
    private int[] likeOrDis;
    private String username;
    private Date create;
    private Date update;
    private Long idMovie;
    private Long idPerson;
    private Long idList;


    public ParsPost(Long id, String title, String content, int[] likeOrDis, String username, Date create, Date update) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.likeOrDis = likeOrDis;
        this.username = username;
        this.create = create;
        this.update = update;
    }
}
