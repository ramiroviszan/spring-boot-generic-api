package com.blackboxdynamics.example.servicesImp;

import com.blackboxdynamics.example.entities.Log;
import com.blackboxdynamics.example.repositories.TestRepository;
import com.blackboxdynamics.example.services.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImp implements TestService {

    @Autowired
    private TestRepository repository;

    @Override
    public Log findByMessage() {
        return repository.findByMessage("Test");
    }
}
