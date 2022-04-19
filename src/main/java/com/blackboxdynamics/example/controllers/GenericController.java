package com.blackboxdynamics.example.controllers;

import com.blackboxdynamics.example.models.PageResponse;
import com.blackboxdynamics.example.services.GenericService;
import com.blackboxdynamics.example.services.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public abstract class GenericController<I, M, E> {

    private final Class<M> classM;
    private final Class<E> classE;

    public GenericController(Class<M> classM, Class<E> classE) {
        this.classM = classM;
        this.classE = classE;
    }

    @GetMapping("/{id}")
    public ResponseEntity<M> get(@PathVariable(name = "id") I id) {
        try {
            E entity = getService().getById(id);
            return new ResponseEntity<>(getMapper().map(entity, this.classM), HttpStatus.OK);
        } catch(NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<PageResponse<M>> getAll(
            @RequestParam(value = "pageNum", defaultValue = "0", required = false) int pageNumber,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize,
            @RequestParam(value = "sort", defaultValue = "id,desc", required = false) String[] sort) {

        try {
            List<Order> sortAndOrders = getSortAndOrder(sort);
            Page<E> result = getService().getAll(pageNumber, pageSize, Sort.by(sortAndOrders));
            PageResponse<M> response = getPageResponse(result);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    private List<Order> getSortAndOrder(String[] sort) {
        List<Order> orders = new ArrayList<Order>();
        if (sort[0].contains(",")) {
            sortMultipleColumns(sort, orders);
        } else {
            orders.add(new Order(getSortDirection(sort[1]), sort[0]));
        }
        return orders;
    }

    private void sortMultipleColumns(String[] sort, List<Order> orders) {
        for (String sortOrder : sort) {
            String[] _sort = sortOrder.split(",");
            orders.add(new Order(getSortDirection(_sort[1]), _sort[0]));
        }
    }

    private Sort.Direction getSortDirection(String direction) {
        if (direction.equals("asc")) {
            return Sort.Direction.ASC;
        } else if (direction.equals("desc")) {
            return Sort.Direction.DESC;
        }
        return Sort.Direction.ASC;
    }

    private PageResponse<M> getPageResponse(Page<E> page) {
        PageResponse<M> response = new PageResponse<>();
        response.setNumber(page.getNumber());
        response.setPageSize(page.getSize());
        response.setTotalPages(page.getTotalPages());
        response.setTotalElements(page.getTotalElements());
        response.setLast(page.isLast());
        List<M> content = page.getContent().stream()
                .map(entity -> getMapper().map(entity, this.classM))
                .collect(Collectors.toList());
        response.setContent(content);
        return response;
    }

    @PostMapping
    public ResponseEntity<M> post(@RequestBody M model) {
        try {
            E entity = getService().create(getMapper().map(model, this.classE));
            return new ResponseEntity<>(getMapper().map(entity, this.classM), HttpStatus.CREATED);
        } catch (ServiceException e) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<M> put(@PathVariable(name = "id") I id, @RequestBody M model) {
        try {
            E entity = getService().update(id, getMapper().map(model, this.classE));
            return new ResponseEntity<>(getMapper().map(entity, this.classM), HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(name = "id") I id) {
        getService().delete(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    protected abstract GenericService<I, E> getService();

    protected abstract ModelMapper getMapper();

}
