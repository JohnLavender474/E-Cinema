package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Admin trainee role def repository.
 */
@Repository
public interface AdminTraineeRoleDefRepository extends UserRoleDefRepository<AdminTraineeRoleDef> {

    /**
     * Find by user with id optional.
     *
     * @param userId the user id
     * @return the optional
     */
    @Query("SELECT a FROM AdminTraineeRoleDef a WHERE a.user.id = ?1")
    Optional<AdminTraineeRoleDef> findByUserWithId(Long userId);

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
