package com.blackboxdynamics.example.repositories;

import com.blackboxdynamics.example.entities.Log;
import org.springframework.stereotype.Repository;

@Repository
public interface LogRepository extends GenericRepository<Long, Log> {

}
