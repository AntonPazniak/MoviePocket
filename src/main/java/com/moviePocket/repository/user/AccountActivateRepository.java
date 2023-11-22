package com.moviePocket.repository.user;

import com.moviePocket.entities.user.AccountActivate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Transactional
@Repository
public interface AccountActivateRepository extends JpaRepository<AccountActivate, Long> {

    AccountActivate findByTokenAccountActivate(String token);

}
