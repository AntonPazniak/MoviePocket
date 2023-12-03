package com.moviePocket.repository.movie;

import com.moviePocket.entities.movie.ProductionCompany;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ProductionCompanyRepository extends JpaRepository<ProductionCompany, Long> {


}
