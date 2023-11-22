package com.moviePocket.entities.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "account_activate", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser"}))
public class AccountActivate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String tokenAccountActivate;

    public AccountActivate(User user, String tokenAccountActivate) {
        this.user = user;
        this.tokenAccountActivate = tokenAccountActivate;
    }
}
