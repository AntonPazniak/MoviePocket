package com.moviePocket.entities.review;

import com.moviePocket.entities.movie.list.MovieList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_list", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idList"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReviewList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idList", nullable = false)
    private MovieList movieList;

    @ManyToOne
    @JoinColumn(name = "idReview", nullable = false)
    private Review review;

    public ReviewList(MovieList movieList, Review review) {
        this.movieList = movieList;
        this.review = review;
    }

}