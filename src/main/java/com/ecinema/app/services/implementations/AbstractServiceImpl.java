package com.ecinema.app.services.implementations;

import com.ecinema.app.repositories.AbstractRepository;
import com.ecinema.app.services.AbstractService;
import com.ecinema.app.domain.entities.AbstractEntity;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * The parent class of service classes handling persistence.
 *
 * @param <E> the persistence type
 * @param <R> the repository type
 */
@Transactional
public abstract class AbstractServiceImpl<E extends AbstractEntity,
        R extends JpaRepository<E, Long> & AbstractRepository>
        implements AbstractService<E> {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /** The Repository. */
    protected final R repository;

    /**
     * Instantiates a new Abstract service.
     *
     * @param repository the repository
     */
    public AbstractServiceImpl(R repository) {
        this.repository = repository;
    }

    /**
     * Called before the entity is deleted.
     *
     * @param entity the entity to handle before deletion.
     */
    protected abstract void onDelete(E entity);

    @Override
    public void delete(E entity) {
        deleteById(entity.getId());
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
    public void deleteAll(Collection<E> entities) {
        // iterate over copy of entities collection to avoid concurrent modification exception.
        for (E entity : List.copyOf(entities)) {
            delete(entity);
        }
    }

    @Override
    public void deleteAll() {
        findAll().forEach(this::onDelete);
        repository.deleteAll();
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
    public void saveAndFlushAll(Iterable<E> entities) {
        repository.saveAllAndFlush(entities);
    }

    @Override
    public void saveAndFlush(E entity) {
        repository.saveAndFlush(entity);
    }

    @Override
    public Optional<E> findById(Long id) {
        return repository.findById(id);
    }

    @Override
    public List<E> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<E> findAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @Override
    public List<Long> findAllIds() {
        return repository.findAllIds();
    }

    @Override
    public Page<Long> findAllIds(Pageable pageable) {
        return UtilMethods.convertListToPage(findAllIds(), pageable);
    }

}
