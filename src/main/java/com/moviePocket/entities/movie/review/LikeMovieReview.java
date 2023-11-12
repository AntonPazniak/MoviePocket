package com.moviePocket.entities.movie.review;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "like_review", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idReview"}))
@Getter
@Setter
@NoArgsConstructor
public class LikeMovieReview extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idReview")
    private ReviewMovie movieReview;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean lickOrDis;

    public LikeMovieReview(ReviewMovie movieReview, User user, boolean lickOrDis) {
        this.movieReview = movieReview;
        this.user = user;
        this.lickOrDis = lickOrDis;
    }
}