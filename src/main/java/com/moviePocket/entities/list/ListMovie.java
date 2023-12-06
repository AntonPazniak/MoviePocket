package com.moviePocket.entities.list;

import com.moviePocket.entities.BaseEntity;
import com.moviePocket.entities.image.ImageEntity;
import com.moviePocket.entities.movie.Movie;
import com.moviePocket.entities.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "list_movie", uniqueConstraints = @UniqueConstraint(columnNames = {"id", "idUser"}))
public class ListMovie extends BaseEntity {

    @Column(nullable = false, unique = true)
    String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    String content;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @ManyToMany
    @JoinTable(
            name = "list_item",
            joinColumns = @JoinColumn(name = "id_list"),
            inverseJoinColumns = @JoinColumn(name = "id_movie"))
    private List<Movie> movies;

    @OneToOne
    @JoinColumn(name = "idImage", referencedColumnName = "id")
    private ImageEntity imageEntity;

    public ListMovie(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }
}
