package com.blackboxdynamics.example.entities;

public interface IEntity<T> {
    void setId(T id);
    T getId();
}

