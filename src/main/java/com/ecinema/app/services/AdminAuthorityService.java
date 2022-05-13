package com.ecinema.app.services;

import com.ecinema.app.domain.EntityDtoConverter;
import com.ecinema.app.domain.dtos.AdminAuthorityDto;
import com.ecinema.app.domain.entities.AdminAuthority;

/**
 * The interface Admin role def service.
 */
public interface AdminAuthorityService extends AbstractUserAuthorityService<AdminAuthority>,
                                               EntityDtoConverter<AdminAuthority, AdminAuthorityDto> {}
