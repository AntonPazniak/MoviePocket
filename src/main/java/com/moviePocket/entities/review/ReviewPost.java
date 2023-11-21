package com.moviePocket.entities.review;

import com.moviePocket.entities.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "review_post", uniqueConstraints = @UniqueConstraint(columnNames = {"idReview", "idPost"}))
@Getter
@Setter
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

    public ReviewPost(Post post, Review review) {
        this.post = post;
        this.review = review;
    }
}
