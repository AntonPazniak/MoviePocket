package com.moviePocket.entities.list;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.movie.Movie;
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
@Table(name = "list_item", uniqueConstraints = @UniqueConstraint(columnNames = {"idMovieList", "idMovie"}))
public class ListItem extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idMovieList", referencedColumnName = "id")
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idMovie")
    private Movie movie;

}
