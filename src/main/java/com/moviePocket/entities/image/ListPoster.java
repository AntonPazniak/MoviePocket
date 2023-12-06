package com.moviePocket.entities.image;


import com.moviePocket.entities.list.ListMovie;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "list_poster")
public class ListPoster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idMovieList", referencedColumnName = "id")
    private ListMovie movieList;

    @ManyToOne
    @JoinColumn(name = "idImageEntity", referencedColumnName = "id")
    private ImageEntity imageEntity;

}
