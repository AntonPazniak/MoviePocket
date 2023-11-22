package com.moviePocket.entities.user;

import com.moviePocket.entities.BaseEntity;
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
@Table(name = "new_email", uniqueConstraints = @UniqueConstraint(columnNames = {"idUser"}))
public class NewEmail extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "idUser", referencedColumnName = "id")
    private User user;

    @Column(nullable = false)
    private String newEmail;

    @Column(nullable = false)
    private String tokenEmailActivate;


}
