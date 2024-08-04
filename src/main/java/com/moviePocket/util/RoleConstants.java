/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.util;

import com.moviePocket.db.entities.user.Role;

import java.util.List;

public class RoleConstants {

    public static final Long ID_ADMIN = 0L;
    public static final String ROLE_ADMIN = "ADMIN";
    public static final Role ADMIN = Role.builder()
            .id(ID_ADMIN)
            .name(ROLE_ADMIN)
            .build();

    public static final Long ID_USER = 1L;
    public static final String ROLE_USER = "USER";
    public static final Role USER = Role.builder()
            .id(ID_USER)
            .name(ROLE_USER)
            .build();

    public static final Long ID_MODERATOR = 2L;
    public static final String ROLE_MODERATOR = "MODERATOR";
    public static final Role MODER = Role.builder()
            .id(ID_MODERATOR)
            .name(ROLE_MODERATOR)
            .build();

    public static final List<Role> ROLES;

    static {
        ROLES = List.of(ADMIN, USER, MODER);
    }
}
