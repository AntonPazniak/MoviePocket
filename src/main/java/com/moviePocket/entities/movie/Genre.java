package com.moviePocket.entities.movie;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "genre", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class Genre {

    @Id
    @SerializedName("id")
    private Long id;

    @SerializedName("name")
    private String name;

    @ManyToMany(mappedBy = "genres")
    private List<Movie> movies;

}
