package com.ecinema.app.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface AbstractRepository {

    @Query("SELECT e.id FROM #{#entityName} e")
    List<Long> findAllIds();

}
