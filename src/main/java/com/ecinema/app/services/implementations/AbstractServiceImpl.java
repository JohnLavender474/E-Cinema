package com.ecinema.app.services.implementations;

import com.ecinema.app.services.AbstractService;
import com.ecinema.app.entities.AbstractEntity;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

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
    public void deleteById(Long id)
            throws NoEntityFoundException {
        E entity = findById(id).orElseThrow(
                () -> new NoEntityFoundException("entity", "id", id));
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
