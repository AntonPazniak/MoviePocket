package com.moviePocket.entities.movie.tracking;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "movie_tracking", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idMovie"}))
@Getter
@Setter
@NoArgsConstructor
public class Tracking extends BaseEntity {

    private Date dateRelease;
    private long idMovie;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    public Tracking(User user, long idMovie, Date dateRelease) {
        this.dateRelease = dateRelease;
        this.idMovie = idMovie;
        this.user = user;
    }
}
