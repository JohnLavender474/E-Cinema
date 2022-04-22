package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Admin trainee role def repository.
 */
@Repository
public interface AdminTraineeRoleDefRepository extends UserRoleDefRepository<AdminTraineeRoleDef> {

    /**
     * Find all by mentor list.
     *
     * @param adminRoleDef the admin role def
     * @return the list
     */
    List<AdminTraineeRoleDef> findAllByMentor(AdminRoleDef adminRoleDef);

    /**
     * Find all by percentage training modules completed less than equal list.
     *
     * @param percentage the percentage
     * @return the list
     */
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedLessThanEqual(Integer percentage);

    /**
     * Find all by percentage training modules completed greater than equal list.
     *
     * @param percentage the percentage
     * @return the list
     */
    List<AdminTraineeRoleDef> findAllByPercentageTrainingModulesCompletedGreaterThanEqual(Integer percentage);

}
