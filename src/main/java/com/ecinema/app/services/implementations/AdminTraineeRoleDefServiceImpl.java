package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.AdminRoleDef;
import com.ecinema.app.domain.entities.AdminTraineeRoleDef;
import com.ecinema.app.repositories.AdminTraineeRoleDefRepository;
import com.ecinema.app.services.AdminTraineeRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class AdminTraineeRoleDefServiceImpl extends UserRoleDefServiceImpl<AdminTraineeRoleDef,
        AdminTraineeRoleDefRepository> implements AdminTraineeRoleDefService {

    public AdminTraineeRoleDefServiceImpl(AdminTraineeRoleDefRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(AdminTraineeRoleDef adminTraineeRoleDef) {
        super.onDelete(adminTraineeRoleDef);
        // detach Admin
        AdminRoleDef adminRoleDef = adminTraineeRoleDef.getMentor();
        if (adminRoleDef != null) {
            adminRoleDef.getTrainees().remove(adminTraineeRoleDef);
            adminTraineeRoleDef.setMentor(null);
        }
    }

    @Override
    public List<AdminTraineeRoleDef> findAllByMentor(AdminRoleDef adminRoleDef) {
        return repository.findAllByMentor(adminRoleDef);
    }

    @Override
    public List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedLessThanEqual(Integer percentage) {
        return repository.findAllByPercentageTrainingModulesCompletedLessThanEqual(percentage);
    }

    @Override
    public List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedGreaterThanEqual(Integer percentage) {
        return repository.findAllByPercentageTrainingModulesCompletedGreaterThanEqual(percentage);
    }

}
