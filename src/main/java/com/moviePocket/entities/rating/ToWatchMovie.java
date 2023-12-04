package com.moviePocket.entities.rating;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
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
@Table(name = "movies_watchlist", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idMovie"}))
public class ToWatchMovie extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

}