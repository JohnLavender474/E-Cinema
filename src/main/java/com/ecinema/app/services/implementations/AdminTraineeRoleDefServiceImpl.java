package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.repositories.AdminTraineeRoleDefRepository;
import com.ecinema.app.services.AdminTraineeRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

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
