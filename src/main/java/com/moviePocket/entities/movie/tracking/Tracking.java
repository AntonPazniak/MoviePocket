package com.moviePocket.entities.movie.tracking;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.user.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "tracking_movie")
@Getter
@Setter
@NoArgsConstructor
public class Tracking extends BaseEntity {

    private Date dateRelease;
    private long idMovie;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
