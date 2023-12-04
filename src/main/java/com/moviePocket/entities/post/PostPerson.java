package com.moviePocket.entities.post;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "post_person", uniqueConstraints = @UniqueConstraint(columnNames = {"idPost", "idPerson"}))
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPerson {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long idPerson;

    @ManyToOne
    @JoinColumn(name = "idPost", nullable = false)
    private Post post;

    public PostPerson(Long idPerson, Post post) {
        this.idPerson = idPerson;
        this.post = post;
    }
}