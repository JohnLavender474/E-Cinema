package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;

import java.util.List;

public interface AdminTraineeRoleDefService extends UserRoleDefService<AdminTraineeRoleDef> {
    List<AdminTraineeRoleDef> findAllByMentor(AdminRoleDef adminRoleDef);
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedLessThanEqual(Integer percentage);
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedGreaterThanEqual(Integer percentage);
}
