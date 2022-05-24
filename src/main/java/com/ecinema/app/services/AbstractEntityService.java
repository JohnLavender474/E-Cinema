package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.entities.AbstractEntity;
import com.ecinema.app.domain.contracts.AbstractDto;
import com.ecinema.app.exceptions.NoEntityFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Transactional
public abstract class AbstractEntityService<E extends AbstractEntity,
        R extends JpaRepository<E, Long>, D extends AbstractDto>
        implements EntityDtoConverter<E, D> {

    protected final R repository;
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    public AbstractEntityService(R repository) {
        this.repository = repository;
    }

    protected abstract void onDelete(E entity);

    @Override
    public D convertToDto(Long id)
            throws NoEntityFoundException {
        return convertToDto(repository.findById(id).orElseThrow(
                () -> new NoEntityFoundException("entity", "id", id)));
    }

    public void save(E entity) {
        repository.save(entity);
    }

    public void saveAll(Iterable<E> entities) {
        repository.saveAll(entities);
    }

    public void delete(E entity) {
        onDelete(entity);
        repository.delete(entity);
    }

    public void deleteAll(Collection<E> entities) {
        List.copyOf(entities).forEach(this::delete);
    }

    public void deleteAll() {
        repository.findAll().forEach(this::onDelete);
        repository.deleteAll();
    }

    public void delete(D dto)
            throws NoEntityFoundException {
        delete(dto.getId());
    }

    public void delete(Long id)
            throws NoEntityFoundException {
        delete(repository.findById(id).orElseThrow(
                () -> new NoEntityFoundException("entity", "id", id)));
    }
    
    public D findById(Long id)
            throws NoEntityFoundException {
        return repository.findById(id).map(this::convertToDto)
                .orElseThrow(() -> new NoEntityFoundException("entity dto", "id", id));
    }

    public List<D> findAll() {
        return repository.findAll()
                         .stream().map(this::convertToDto)
                         .collect(Collectors.toList());
    }

    public Page<D> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(this::convertToDto);
    }

}
