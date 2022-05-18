package com.ecinema.app.services;

import com.ecinema.app.domain.dtos.CustomerAuthorityDto;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.ModeratorAuthority;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;

import java.util.List;
import java.util.Set;

/**
 * The interface Moderator role def service.
 */
public interface ModeratorAuthorityService extends AbstractUserAuthorityService<ModeratorAuthority> {

    /**
     * Censor customer with id.
     *
     * @param moderatorRoleDefId the moderator role def id
     * @param customerRoleDefId  the customer role def id
     * @throws NoEntityFoundException the no entity found exception
     * @throws ClashException         the clash exception
     */
    void setCustomerCensoredStatus(Long moderatorRoleDefId, Long customerRoleDefId, boolean censorStatus)
            throws NoEntityFoundException, ClashException;

    /**
     * Censored customer dtos of user with id list.
     *
     * @param userId the user id
     * @return the list
     * @throws NoEntityFoundException the no entity found exception
     */
    List<CustomerAuthorityDto> censoredCustomerDtosOfUserWithId(Long userId)
            throws NoEntityFoundException;

}
