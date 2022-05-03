package com.ecinema.app.services;

import com.ecinema.app.domain.entities.AbstractEntity;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * The interface Abstract service.
 *
 * @param <E> the type parameter
 */
public interface AbstractService<E extends AbstractEntity> {

    /**
     * Find by id optional.
     *
     * @param id the id
     * @return the optional
     */
    Optional<E> findById(Long id);

    /**
     * Find all list.
     *
     * @return the list
     */
    List<E> findAll();

    /**
     * Find all page.
     *
     * @param pageable the pageable
     * @return the page
     */
    Page<E> findAll(Pageable pageable);

    /**
     * Find all ids list.
     *
     * @return the list
     */
    List<Long> findAllIds();

    /**
     * Find all ids list.
     *
     * @param pageable the pageable
     * @return the list
     */
    Page<Long> findAllIds(Pageable pageable);

    /**
     * Deletes the provided entity. This method should fetch the id of the entity and call {@link #deleteById(Long)}
     * to ensure no persistence exceptions occur due the provided entity originating from outside of transaction bounds.
     *
     * @param entity the entity
     */
    void delete(E entity);

    /**
     * Delete by id.
     *
     * @param id the id
     * @throws NoEntityFoundException the no entity found exception
     */
    void deleteById(Long id)
            throws NoEntityFoundException;

    /**
     * Delete all.
     *
     * @param entities the entities
     */
    void deleteAll(Collection<E> entities);

    /**
     * Delete all.
     */
    void deleteAll();

    /**
     * Save.
     *
     * @param entity the entity
     */
    void save(E entity);

    /**
     * Save and get id long.
     *
     * @param entity the entity
     * @return the long
     */
    Long saveAndGetId(E entity);

    /**
     * Save all.
     *
     * @param entities the entities
     */
    void saveAll(Iterable<E> entities);

    /**
     * Save all and get ids list.
     *
     * @param entities the entities
     * @return the list
     */
    List<Long> saveAllAndGetIds(Iterable<E> entities);

    /**
     * Save and flush.
     *
     * @param entity the entity
     */
    void saveAndFlush(E entity);

    /**
     * Save flush and get id.
     *
     * @param entity the entity
     */
    Long saveFlushAndGetId(E entity);

    /**
     * Save and flush all.
     *
     * @param entities the entities
     */
    void saveAndFlushAll(Iterable<E> entities);

    /**
     * Save flush all and get ids.
     *
     * @param entities the entities
     */
    List<Long> saveFlushAllAndGetIds(Iterable<E> entities);

}
