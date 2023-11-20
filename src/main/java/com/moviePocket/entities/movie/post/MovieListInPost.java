package com.moviePocket.entities.movie.post;

import com.moviePocket.entities.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "movieList_in_post", uniqueConstraints = @UniqueConstraint(columnNames = {"idPost", "idMovieList"}))
public class MovieListInPost extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idPost")
    private Post post;

    @Column(nullable = false)
    private long idMovieList;
}
