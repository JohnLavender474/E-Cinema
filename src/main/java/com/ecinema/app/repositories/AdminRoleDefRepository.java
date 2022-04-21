package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleDefRepository extends UserRoleDefRepository<AdminRoleDef> {

    @Query("SELECT a FROM AdminRoleDef a JOIN a.theatersBeingManaged t WHERE t = ?1")
    List<AdminRoleDef> findDistinctByTheatersBeingManagedContains(Theater theater);

    @Query("SELECT a FROM AdminRoleDef a JOIN a.trainees t WHERE t = ?1")
    List<AdminRoleDef> findDistinctByTraineesContains(AdminTraineeRoleDef adminTraineeRoleDef);

}
