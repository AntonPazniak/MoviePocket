package com.moviePocket.entities.post;

import com.moviePocket.entities.movie.Movie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"idPost", "idMovie"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostMovie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    public PostMovie(Movie movie, Post post) {
        this.movie = movie;
        this.post = post;
    }
}