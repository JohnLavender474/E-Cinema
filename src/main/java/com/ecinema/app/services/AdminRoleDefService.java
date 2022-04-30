package com.ecinema.app.services;

import com.ecinema.app.dtos.AdminRoleDefDto;
import com.ecinema.app.entities.AdminRoleDef;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.utils.Converter;

/**
 * The interface Admin role def service.
 */
public interface AdminRoleDefService extends UserRoleDefService<AdminRoleDef>, Converter<AdminRoleDefDto, Long> {

    /**
     * Add theater to admin role def.
     *
     * @param theaterId      the theater id
     * @param adminRoleDefId the admin role def id
     * @throws NoEntityFoundException the no entity found exception
     */
    void addTheaterToAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException;

    /**
     * Remove theater from admin role def.
     *
     * @param theaterId      the theater id
     * @param adminRoleDefId the admin role def id
     * @throws NoEntityFoundException the no entity found exception
     */
    void removeTheaterFromAdminRoleDef(Long theaterId, Long adminRoleDefId)
            throws NoEntityFoundException;

    /**
     * Add trainee to admin role def.
     *
     * @param adminTraineeRoleDefId the admin trainee role def id
     * @param adminRoleDefId        the admin role def id
     * @throws NoEntityFoundException the no entity found exception
     */
    void addTraineeToAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException;

    /**
     * Remove trainee from admin role def.
     *
     * @param adminTraineeRoleDefId the admin trainee role def id
     * @param adminRoleDefId        the admin role def id
     * @throws NoEntityFoundException the no entity found exception
     */
    void removeTraineeFromAdminRoleDef(Long adminTraineeRoleDefId, Long adminRoleDefId)
            throws NoEntityFoundException;

}
