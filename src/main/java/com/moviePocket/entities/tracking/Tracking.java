package com.moviePocket.entities.tracking;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "movie_tracking", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idMovie"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Tracking extends BaseEntity {

    private LocalDate dateRelease;

    @ManyToOne
    @JoinColumn(name = "idMovie", referencedColumnName = "id")
    private Movie movie;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;


}
