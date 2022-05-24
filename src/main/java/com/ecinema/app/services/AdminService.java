package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.AdminDto;
import com.ecinema.app.domain.entities.Admin;
import com.ecinema.app.repositories.AdminRepository;
import com.ecinema.app.util.UtilMethods;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminService extends UserAuthorityService<Admin, AdminRepository, AdminDto> {

    @Autowired
    public AdminService(AdminRepository repository) {
        super(repository);
        logger.debug(UtilMethods.getLoggingSubjectDelimiterLine());
        logger.debug("Admin Service: autowire repository: " + repository);
    }

    @Override
    protected void onDelete(Admin entity) {
        // detach User
        super.onDelete(entity);
    }

    @Override
    public AdminDto convertToDto(Admin admin) {
        AdminDto adminDto = new AdminDto();
        fillCommonUserAuthorityDtoFields(admin, adminDto);
        return adminDto;
    }

}
