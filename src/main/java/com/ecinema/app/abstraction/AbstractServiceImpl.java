package com.ecinema.app.abstraction;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractServiceImpl<E extends AbstractEntity, R extends JpaRepository<E, Long>> implements AbstractService<E> {

    protected final R repository;

    public AbstractServiceImpl(R repository) {
        this.repository = repository;
    }

    @Override
    public void deleteAll() {
        repository.deleteAll();
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public void delete(E entity) {
        repository.delete(entity);
    }

    @Override
    public void save(E entity) {
        repository.save(entity);
    }

    @Override
    public void saveAll(Iterable<E> entities) {
        repository.saveAll(entities);
    }

    @Override
    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }


}
