package com.moviePocket.entities.post;

import com.moviePocket.entities.movie.list.MovieList;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_list", uniqueConstraints = @UniqueConstraint(columnNames = {"idPost", "idList"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idList", nullable = false)
    private MovieList movieList;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    public PostList(MovieList movieList, Post post) {
        this.movieList = movieList;
        this.post = post;
    }

}