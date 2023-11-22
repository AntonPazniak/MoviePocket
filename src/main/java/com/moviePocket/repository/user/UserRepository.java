package com.moviePocket.repository.user;

import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String mail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsernameAndAccountActive(String username, boolean isActive);
}
