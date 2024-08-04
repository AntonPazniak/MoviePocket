/*
 * ******************************************************
 *  Copyright (C)  MoviePocket <prymakdn@gmail.com>
 *  This file is part of MoviePocket.
 *  MoviePocket can not be copied and/or distributed without the express
 *  permission of Danila Prymak, Alexander Trafimchyk and Anton Pozniak
 * *****************************************************
 */

package com.moviePocket.db.entities.review;

import com.moviePocket.db.entities.post.Post;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "review_post", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idPost"}))
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    @ManyToOne
    @JoinColumn(name = "idReview", nullable = false)
    private Review review;

}
