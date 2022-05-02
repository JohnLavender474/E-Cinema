package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.AdminRoleDefDto;
import com.ecinema.app.domain.entities.AdminRoleDef;
import com.ecinema.app.exceptions.NoEntityFoundException;

/**
 * The interface Admin role def service.
 */
public interface AdminRoleDefService extends UserRoleDefService<AdminRoleDef>,
                                             EntityDtoConverter<AdminRoleDef, AdminRoleDefDto> {}
