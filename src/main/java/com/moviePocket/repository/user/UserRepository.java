package com.moviePocket.repository.user;

import com.moviePocket.entities.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String mail);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
    User findByUsernameAndAccountActive(String username, boolean isActive);

    @Query("SELECT u FROM User u WHERE u.username LIKE :partialUsername%")
    List<User> findByPartialUsername(@Param("partialUsername") String partialUsername);
}
