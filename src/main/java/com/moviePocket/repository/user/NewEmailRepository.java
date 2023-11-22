package com.moviePocket.repository.user;

import com.moviePocket.entities.user.NewEmail;
import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface NewEmailRepository extends JpaRepository<NewEmail, Long> {

    NewEmail findByTokenEmailActivate(String token);

    NewEmail findByUser(User user);

}
