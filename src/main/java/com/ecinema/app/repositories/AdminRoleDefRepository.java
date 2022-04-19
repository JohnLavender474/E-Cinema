package com.ecinema.app.repositories;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.entities.AdminTraineeRoleDef;
import com.ecinema.app.entities.Theater;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRoleDefRepository extends UserRoleDefRepository<AdminRoleDef> {
    List<AdminRoleDef> findDistinctByTheatersContains(Theater theater);
    List<AdminRoleDef> findDistinctByTraineesContains(AdminTraineeRoleDef adminTraineeRoleDef);
}
