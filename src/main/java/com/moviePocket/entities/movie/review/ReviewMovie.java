package com.moviePocket.entities.movie.review;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idMovie"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idMovie;

    @ManyToOne
    @JoinColumn(name = "idReview", nullable = false)
    private Review review;

    public ReviewMovie(Long idMovie, Review movieReview) {
        this.idMovie = idMovie;
        this.review = movieReview;
    }
}
