package com.blackboxdynamics.example.models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Data
public class LogModel {

    private long id;
    private String message;
    private Date createdAt;

}
