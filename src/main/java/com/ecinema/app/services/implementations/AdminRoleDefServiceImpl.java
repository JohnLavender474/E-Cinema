package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.AdminRoleDefDto;
import com.ecinema.app.domain.entities.AdminRoleDef;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.AdminRoleDefRepository;
import com.ecinema.app.services.AdminRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * The type Admin role def service.
 */
@Service
@Transactional
public class AdminRoleDefServiceImpl extends UserRoleDefServiceImpl<AdminRoleDef, AdminRoleDefRepository>
        implements AdminRoleDefService {

    /**
     * Instantiates a new Admin role def service.
     *
     * @param repository                 the repository
     */
    public AdminRoleDefServiceImpl(AdminRoleDefRepository repository) {
        super(repository);
    }

    @Override
    public AdminRoleDefDto convertToDto(Long id)
            throws NoEntityFoundException {
        AdminRoleDef adminRoleDef = findById(id).orElseThrow(
                () -> new NoEntityFoundException("admin role def", "id", id));
        AdminRoleDefDto adminRoleDefDto = new AdminRoleDefDto();
        adminRoleDefDto.setId(adminRoleDef.getId());
        logger.debug("Converted " + adminRoleDef + " to DTO: " + adminRoleDefDto);
        return adminRoleDefDto;
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {}

    @Override
    public void onDeleteInfo(AdminRoleDef adminRoleDef, Collection<String> info) {}

}
