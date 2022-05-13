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
public interface EntityDtoConverter<E extends AbstractEntity, D extends AbstractDto> {

    /**
     * Convert to dto d.
     *
     * @param entity the entity
     * @return the d
     * @throws NoEntityFoundException the no entity found exception
     */
    default D convertToDto(E entity)
            throws NoEntityFoundException {
        return convertIdToDto(entity.getId());
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
            D d = convertIdToDto(e.getId());
            dtos.add(d);
        }
        return dtos;
    }

    /**
     * Convert ids to dto list.
     *
     * @param ids the ids
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    default List<D> convertIdsToDto(Iterable<Long> ids)
        throws NoEntityFoundException {
        List<D> dtos = new ArrayList<>();
        for (Long id : ids) {
            D d = convertIdToDto(id);
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
    D convertIdToDto(Long id)
            throws NoEntityFoundException;

}
