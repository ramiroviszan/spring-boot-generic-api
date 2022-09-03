package com.blackboxdynamics.example.repositories;

import com.blackboxdynamics.example.entities.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface TestRepository extends JpaRepository {

    @Query("SELECT l From Log l WHERE l.message = ?1")
    Log findByMessage(String message);
}
