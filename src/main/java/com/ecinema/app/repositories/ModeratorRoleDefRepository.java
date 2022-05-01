package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.ModeratorRoleDef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Moderator role def repository.
 */
@Repository
public interface ModeratorRoleDefRepository extends UserRoleDefRepository<ModeratorRoleDef> {

    /**
     * Find all ids list.
     *
     * @return the list
     */
    @Query("SELECT m.id FROM ModeratorRoleDef m")
    List<Long> findAllIds();

}
