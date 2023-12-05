package com.moviePocket.entities.list;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.movie.Genre;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_genres")
public class ListGenres extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idMovieList", referencedColumnName = "id")
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idGenre", referencedColumnName = "id")
    private Genre genre;

}