package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.ModeratorAuthority;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Moderator role def repository.
 */
@Repository
public interface ModeratorAuthorityRepository extends AbstractUserAuthorityRepository<ModeratorAuthority> {

    /**
     * Find all ids list.
     *
     * @return the list
     */
    @Query("SELECT m.id FROM ModeratorAuthority m")
    List<Long> findAllIds();

}
