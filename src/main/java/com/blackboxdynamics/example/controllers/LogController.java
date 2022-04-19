package com.blackboxdynamics.example.controllers;

import com.blackboxdynamics.example.entities.Log;
import com.blackboxdynamics.example.models.LogModel;
import com.blackboxdynamics.example.repositories.GenericRepository;
import com.blackboxdynamics.example.repositories.LogRepository;
import com.blackboxdynamics.example.services.GenericService;
import com.blackboxdynamics.example.servicesImp.GenericServiceImp;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/logs")
public class LogController extends GenericController<Long, LogModel, Log> {

    @Autowired
    private GenericService<Long, Log> service;

    @Autowired
    private ModelMapper mapper;

    public LogController() {
        super(LogModel.class, Log.class);
    }

    @Override
    protected GenericService<Long, Log> getService() {
        return service;
    }

    @Override
    protected ModelMapper getMapper() {
        return mapper;
    }
}
