package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminTraineeRoleDefRepository extends UserRoleDefRepository<AdminTraineeRoleDef> {
    List<AdminTraineeRoleDef> findAllByMentor(AdminRoleDef adminRoleDef);
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedLessThanEqual(Integer percentage);
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedGreaterThanEqual(Integer percentage);
}
