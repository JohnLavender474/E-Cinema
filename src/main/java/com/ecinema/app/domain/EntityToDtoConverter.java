package com.ecinema.app.domain;

import com.ecinema.app.domain.dtos.AbstractDto;
import com.ecinema.app.domain.entities.AbstractEntity;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.util.ArrayList;
import java.util.List;

/**
 * The interface Dto converter service.
 *
 * @param <E> the type parameter
 * @param <D> the type parameter
 */
public interface EntityToDtoConverter<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Convert to dto d.
     *
     * @param entity the entity
     * @return the d
     * @throws NoEntityFoundException the no entity found exception
     */
    default D convertToDto(E entity)
            throws NoEntityFoundException {
        return convertToDto(entity.getId());
    }

    /**
     * Convert to dto list.
     *
     * @param entities the entities
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    default List<D> convertToDto(Iterable<E> entities)
            throws NoEntityFoundException {
        List<D> dtos = new ArrayList<>();
        for (E e : entities) {
            D d = convertToDto(e.getId());
            dtos.add(d);
        }
        return dtos;
    }

    /**
     * Convert by id d.
     *
     * @param id the id
     * @return the d
     * @throws NoEntityFoundException the no entity found exception
     */
    D convertToDto(Long id)
            throws NoEntityFoundException;

}
