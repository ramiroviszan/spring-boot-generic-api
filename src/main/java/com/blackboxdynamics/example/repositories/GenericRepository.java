package com.blackboxdynamics.example.repositories;

import com.blackboxdynamics.example.entities.IEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GenericRepository<I, T extends IEntity<I>> extends JpaRepository<T, I> {
}
