package com.moviePocket.db.entities;

import com.moviePocket.db.entities.user.User;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Reaction extends BaseEntity {

    private boolean reaction;

    @ManyToOne
    @JoinColumn(name = "idUser")
    private User user;

}
