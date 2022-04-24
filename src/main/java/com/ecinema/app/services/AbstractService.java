package com.ecinema.app.services;

import com.ecinema.app.entities.AbstractEntity;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
     */
    void deleteAll();

    /**
     * Save.
     *
     * @param entity the entity
     */
    void save(E entity);

    /**
     * Save all.
     *
     * @param entities the entities
     */
    void saveAll(Iterable<E> entities);

}
