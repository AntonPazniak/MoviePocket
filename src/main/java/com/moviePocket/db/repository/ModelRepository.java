package com.moviePocket.db.repository;

import com.moviePocket.db.entities.Module;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ModelRepository extends JpaRepository<Module, Long> {


}
