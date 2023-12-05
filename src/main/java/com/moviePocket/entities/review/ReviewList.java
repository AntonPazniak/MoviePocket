package com.moviePocket.entities.review;

import com.moviePocket.entities.list.ListMovie;
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
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idReview", nullable = false)
    private Review review;

    public ReviewList(ListMovie movieList, Review review) {
        this.movieList = movieList;
        this.review = review;
    }

}