package com.moviePocket.entities.movie;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;


@Getter
@Setter
@Entity
@Table(name = "companies", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class ProductionCompany {

    @Id
    @SerializedName("id")
    @Column(name = "id")
    private Long id;

    @SerializedName("logo_path")
    private String logoPath;

    @SerializedName("name")
    private String name;

    @SerializedName("origin_country")
    private String originCountry;

    @ManyToMany(mappedBy = "production_companies")
    private List<Movie> movies;


}
