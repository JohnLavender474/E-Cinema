package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Admin role def repository.
 */
@Repository
public interface AdminRoleDefRepository extends UserRoleDefRepository<AdminRoleDef> {

    /**
     * Find distinct by theaters being managed contains list.
     *
     * @param theater the theater
     * @return the list
     */
    @Query("SELECT DISTINCT a FROM AdminRoleDef a JOIN a.theatersBeingManaged t WHERE t = ?1")
    List<AdminRoleDef> findDistinctByTheatersBeingManagedContains(Theater theater);

    /**
     * Find distinct by trainees contains list.
     *
     * @param adminTraineeRoleDef the admin trainee role def
     * @return the list
     */
    @Query("SELECT DISTINCT a FROM AdminRoleDef a JOIN a.trainees t WHERE t = ?1")
    List<AdminRoleDef> findDistinctByTraineesContains(AdminTraineeRoleDef adminTraineeRoleDef);

}
