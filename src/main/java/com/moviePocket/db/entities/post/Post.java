/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.post;

import com.moviePocket.db.entities.BaseEntity;
import com.moviePocket.db.entities.Module;
import com.moviePocket.db.entities.review.ReviewPost;
import com.moviePocket.db.entities.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "post", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "idUser"}))
public class Post extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int idModule;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReactionPost> reactions;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReviewPost> reviews;

    @ManyToOne
    @JoinColumn(name = "module_id") // specify the actual foreign key column name
    private Module module;


    @Column(nullable = false)
    private Long idItem;

}
