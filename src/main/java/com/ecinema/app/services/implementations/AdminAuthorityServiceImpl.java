package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.AdminAuthorityDto;
import com.ecinema.app.domain.entities.AdminAuthority;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.AdminAuthorityRepository;
import com.ecinema.app.services.AdminAuthorityService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;

/**
 * The type Admin role def service.
 */
@Service
@Transactional
public class AdminAuthorityServiceImpl extends AbstractUserAuthorityServiceImpl<
        AdminAuthority, AdminAuthorityRepository> implements AdminAuthorityService {

    /**
     * Instantiates a new Admin role def service.
     *
     * @param repository                 the repository
     */
    public AdminAuthorityServiceImpl(AdminAuthorityRepository repository) {
        super(repository);
    }

    @Override
    public AdminAuthorityDto convertIdToDto(Long id)
            throws NoEntityFoundException {
        AdminAuthority adminAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("admin role def", "id", id));
        AdminAuthorityDto adminAuthorityDto = new AdminAuthorityDto();
        adminAuthorityDto.setAuthorityId(adminAuthority.getId());
        logger.debug("Converted " + adminAuthority + " to DTO: " + adminAuthorityDto);
        return adminAuthorityDto;
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        AdminAuthority adminAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("admin role def", "id", id));
        onDeleteInfo(adminAuthority, info);
    }

    @Override
    public void onDeleteInfo(AdminAuthority adminAuthority, Collection<String> info) {
        String username = adminAuthority.getUser().getUsername();
        info.add(username + " will lose admin privileges");
    }

}
