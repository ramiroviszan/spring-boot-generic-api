package com.blackboxdynamics.example.services;

public class ServiceException extends Exception{

    public ServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}
