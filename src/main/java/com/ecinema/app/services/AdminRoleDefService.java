package com.ecinema.app.services;

import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.utils.exceptions.NoEntityFoundException;

public interface AdminRoleDefService extends UserRoleDefService<AdminRoleDef> {

    void addTheaterToAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException;

    void removeTheaterFromAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException;

    void addTraineeToAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException;

    void removeTraineeFromAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException;

}
