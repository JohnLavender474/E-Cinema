package com.ecinema.app.repositories;

import com.ecinema.app.entities.ModeratorRoleDef;
import org.springframework.stereotype.Repository;

/**
 * The interface Moderator role def repository.
 */
@Repository
public interface ModeratorRoleDefRepository extends UserRoleDefRepository<ModeratorRoleDef> {}
