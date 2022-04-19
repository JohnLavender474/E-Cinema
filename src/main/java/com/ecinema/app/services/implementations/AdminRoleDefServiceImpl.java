package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.repositories.AdminRoleDefRepository;
import com.ecinema.app.services.AdminRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminRoleDefServiceImpl extends UserRoleDefServiceImpl<AdminRoleDef,
        AdminRoleDefRepository> implements AdminRoleDefService {

    public AdminRoleDefServiceImpl(AdminRoleDefRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(AdminRoleDef adminRoleDef) {

    }

}
