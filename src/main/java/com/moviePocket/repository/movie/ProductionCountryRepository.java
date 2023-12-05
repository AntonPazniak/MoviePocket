package com.moviePocket.repository.movie;

import com.moviePocket.entities.movie.ProductionCountry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductionCountryRepository extends JpaRepository<ProductionCountry, Long> {

    boolean existsByIso31661(String s);
}