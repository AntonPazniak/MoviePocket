package com.moviePocket.util;

import com.moviePocket.db.entities.Module;

import java.util.List;

public class ModelsConstant {

    public static final List<Module> modules;

    public static final Long MOVIE_ID = 0L;
    public static final String MOVIE_TITLE = "Movie";
    public static final Module movie = Module.builder()
            .id(MOVIE_ID)
            .name(MOVIE_TITLE)
            .build();

    public static final Long REVIEW_ID = 1L;
    public static final String REVIEW_TITLE = "Review";
    public static final Module review = Module.builder()
            .id(REVIEW_ID)
            .name(REVIEW_TITLE)
            .build();

    public static final Long LIST_ID = 2L;
    public static final String List_TITLE = "List";
    public static final Module list = Module.builder()
            .id(LIST_ID)
            .name(List_TITLE)
            .build();

    public static final Long POST_ID = 3L;
    public static final String POST_TITLE = "Post";
    public static final Module post = Module.builder()
            .id(POST_ID)
            .name(POST_TITLE)
            .build();

    public static final Long PERSON_ID = 4L;
    public static final String PERSON_TITLE = "Person";
    public static final Module person = Module.builder()
            .id(PERSON_ID)
            .name(PERSON_TITLE)
            .build();

    static {
        modules = List.of(movie, review, list, post, person);
    }

}
