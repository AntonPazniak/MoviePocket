package com.moviePocket.entities.post;

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

    @Column(nullable = false)
    private Long idMovie;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    public PostMovie(Long idMovie, Post post) {
        this.idMovie = idMovie;
        this.post = post;
    }
}