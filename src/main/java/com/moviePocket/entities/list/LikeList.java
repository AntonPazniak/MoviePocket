package com.moviePocket.entities.list;

import com.moviePocket.entities.BaseEntity;
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
@Table(name = "like_list", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser", "idList"}))
public class LikeList extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idList")
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

    private boolean lickOrDis;
}