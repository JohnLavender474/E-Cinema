package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.repositories.AdminTraineeRoleDefRepository;
import com.ecinema.app.services.AdminTraineeRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AdminTraineeRoleDefServiceImpl extends UserRoleDefServiceImpl<AdminTraineeRoleDef,
        AdminTraineeRoleDefRepository> implements AdminTraineeRoleDefService {

    public AdminTraineeRoleDefServiceImpl(AdminTraineeRoleDefRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(AdminTraineeRoleDef adminTraineeRoleDef) {

    }

}
