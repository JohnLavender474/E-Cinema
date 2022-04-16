package com.ecinema.app.abstraction;

import java.util.List;
import java.util.Optional;

public interface AbstractService<E extends AbstractEntity> {
    Optional<E> findById(Long id);
    List<E> findAll();
    void deleteAll();
    void deleteById(Long id);
    void delete(E entity);
    void save(E entity);
    void saveAll(Iterable<E> entities);
}
