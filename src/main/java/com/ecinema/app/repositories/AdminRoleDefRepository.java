package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.AdminRoleDef;
import org.springframework.stereotype.Repository;

/**
 * The interface Admin role def repository.
 */
@Repository
public interface AdminRoleDefRepository extends UserRoleDefRepository<AdminRoleDef> {}
