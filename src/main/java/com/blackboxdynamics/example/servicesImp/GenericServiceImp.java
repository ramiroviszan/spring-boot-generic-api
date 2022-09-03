package com.blackboxdynamics.example.servicesImp;

import com.blackboxdynamics.example.entities.IEntity;
import com.blackboxdynamics.example.entities.Log;
import com.blackboxdynamics.example.services.GenericService;
import com.blackboxdynamics.example.repositories.GenericRepository;
import com.blackboxdynamics.example.services.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class GenericServiceImp<I, T extends IEntity<I>> implements GenericService<I, T> {

    @Autowired
    private GenericRepository<I, T> repository;

    @Override
    public T getById(I id) {
        return repository.findById(id).orElseThrow();
    }

    @Override
    public Page<T> getAll(int pageNum, int pageSize, Sort sortAndOrders) {
        Pageable pageable = PageRequest.of(pageNum, pageSize, sortAndOrders);
        return repository.findAll(pageable);
    }

    @Override
    public T create(T entity) throws ServiceException {
        Optional<T> existent = repository.findById(entity.getId());
        if(existent.isPresent()) {
            throw new ServiceException("Entity already exists", null);
        }
        return repository.save(entity);
    }

    @Override
    public T update(I id, T entity) {
        getById(id);
        entity.setId(id);
        return repository.save(entity);
    }

    @Override
    public void delete(I id) {
        Optional<T> entity = repository.findById(id);
        if(entity.isPresent()) {
            repository.delete(entity.get());
        }
    }
}
