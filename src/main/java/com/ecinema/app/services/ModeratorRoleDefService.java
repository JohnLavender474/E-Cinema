package com.ecinema.app.services;

import com.ecinema.app.domain.entities.ModeratorRoleDef;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;

/**
 * The interface Moderator role def service.
 */
public interface ModeratorRoleDefService extends UserRoleDefService<ModeratorRoleDef> {

    /**
     * Censor customer with id.
     *
     * @param moderatorRoleDefId the moderator role def id
     * @param customerRoleDefId  the customer role def id
     */
    void censorCustomerWithId(Long moderatorRoleDefId, Long customerRoleDefId)
            throws NoEntityFoundException, ClashException;

}
