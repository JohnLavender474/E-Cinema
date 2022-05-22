package com.ecinema.app.domain;

import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.domain.entities.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface EntityDtoConverter<E extends AbstractEntity, D extends AbstractDto> {

    D convertToDto(E entity);

    D convertToDto(Long id);

    default List<D> convertToDto(Collection<E> entities) {
        return entities.stream().map(this::convertToDto)
                       .collect(Collectors.toList());
    }

}
