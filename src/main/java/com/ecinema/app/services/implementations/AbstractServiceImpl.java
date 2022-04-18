package com.ecinema.app.services.implementations;

import com.ecinema.app.services.AbstractService;
import com.ecinema.app.entities.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public abstract class AbstractServiceImpl<E extends AbstractEntity, R extends JpaRepository<E, Long>> implements AbstractService<E> {

    protected final R repository;

    public AbstractServiceImpl(R repository) {
        this.repository = repository;
    }

    protected abstract void onDelete(E e);

    @Override
    public void deleteAll() {
        findAll().forEach(this::onDelete);
        repository.deleteAll();
    }

    @Override
    public boolean deleteById(Long id) {
        Optional<E> optionalEntity = findById(id);
        if (optionalEntity.isEmpty()) {
            return false;
        }
        E entity = optionalEntity.get();
        delete(entity);
        return true;
    }

    @Override
    public void delete(E entity) {
        onDelete(entity);
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
