package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;

import java.util.List;

public interface AdminTraineeRoleDefService extends UserRoleDefService<AdminTraineeRoleDef> {

    /**
     * Find all {@link AdminTraineeRoleDef} associated with the provided {@link AdminRoleDef}.
     *
     * @param adminRoleDef the adminRoleDef associated with the AdminTraineeRoleDef in
     *                     the returned list.
     * @return the list of AdminTraineeRoleDef associated with adminRoleDef.
     */
    List<AdminTraineeRoleDef> findAllByMentor(AdminRoleDef adminRoleDef);

    /**
     * Find all by percentage training modules completed less than or equal to the provided value.
     *
     * @param percentage the percentage to check against.
     * @return the list of {@link AdminTraineeRoleDef}.
     */
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedLessThanEqual(Integer percentage);

    /**
     * Find all by percentage training modules completed greater than or equal to the provided value.
     *
     * @param percentage the percentage to check against.
     * @return the list of {@link AdminTraineeRoleDef}.
     */
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedGreaterThanEqual(Integer percentage);

}
