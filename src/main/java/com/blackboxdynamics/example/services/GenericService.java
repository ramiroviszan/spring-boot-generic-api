package com.blackboxdynamics.example.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;


public interface GenericService<I, T> {

    T getById(I id);
    Page<T> getAll(int pageNum, int pageSize, Sort sortAndOrders);
    T create(T entity) throws ServiceException;
    T update(I id, T entity);
    void delete(I id);
}
