package com.ecinema.app.services;

import com.ecinema.app.entities.AbstractEntity;

import java.util.List;
import java.util.Optional;

public interface AbstractService<E extends AbstractEntity> {
    Optional<E> findById(Long id);
    List<E> findAll();
    boolean deleteById(Long id);
    void deleteAll();
    void delete(E entity);
    void save(E entity);
    void saveAll(Iterable<E> entities);
}
