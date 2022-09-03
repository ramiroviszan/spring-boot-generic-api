package com.blackboxdynamics.example.controllers;

import com.blackboxdynamics.example.entities.Log;
import com.blackboxdynamics.example.models.LogModel;
import com.blackboxdynamics.example.services.GenericService;
import com.blackboxdynamics.example.services.TestService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/logs")
public class TestController  {

    @Autowired
    private TestService service;

    @Autowired
    private ModelMapper mapper;


    @GetMapping(path="/test")
    public ResponseEntity<LogModel> getByMessage() {
        return new ResponseEntity<>(mapper.map(service.findByMessage(), LogModel.class), HttpStatus.OK);
    }
}