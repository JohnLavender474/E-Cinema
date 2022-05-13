package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.AdminAuthority;
import org.springframework.stereotype.Repository;

/**
 * The interface Admin role def repository.
 */
@Repository
public interface AdminAuthorityRepository extends AbstractUserAuthorityRepository<AdminAuthority> {}
