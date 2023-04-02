package com.moviePocket.entities;


import javax.persistence.*;
import lombok.*;


@Entity
@Table(name = "user")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String email;

    @Lob
    private String bio;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

}