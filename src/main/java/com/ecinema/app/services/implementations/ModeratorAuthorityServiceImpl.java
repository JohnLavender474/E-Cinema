package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.dtos.CustomerAuthorityDto;
import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.ModeratorAuthority;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ModeratorAuthorityRepository;
import com.ecinema.app.services.CustomerAuthorityService;
import com.ecinema.app.services.ModeratorAuthorityService;
import com.ecinema.app.utils.UtilMethods;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * The type Moderator authority service.
 */
@Service
@Transactional
public class ModeratorAuthorityServiceImpl extends AbstractUserAuthorityServiceImpl<ModeratorAuthority,
        ModeratorAuthorityRepository> implements ModeratorAuthorityService {

    private final CustomerAuthorityService customerAuthorityService;
    private final Logger logger = LoggerFactory.getLogger(ModeratorAuthorityService.class);

    /**
     * Instantiates a new Moderator authority service.
     *
     * @param repository               the repository
     * @param customerAuthorityService the customer authority service
     */
    public ModeratorAuthorityServiceImpl(ModeratorAuthorityRepository repository,
                                         CustomerAuthorityService customerAuthorityService) {
        super(repository);
        this.customerAuthorityService = customerAuthorityService;
    }

    @Override
    protected void onDelete(ModeratorAuthority AbstractUserAuthority) {
        // detach User
        super.onDelete(AbstractUserAuthority);
        // uncensor and detach censored Customers
        logger.debug("Detach moderator from customers censored by moderator");
        Iterator<CustomerAuthority> customerRoleDefIterator =
                AbstractUserAuthority.getCensoredCustomers().iterator();
        while (customerRoleDefIterator.hasNext()) {
            CustomerAuthority customerAuthority = customerRoleDefIterator.next();
            logger.debug("Detaching: " + customerAuthority);
            customerAuthority.setCensoredBy(null);
            customerRoleDefIterator.remove();
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        ModeratorAuthority moderatorAuthority = findById(id).orElseThrow(
                () -> new NoEntityFoundException("moderator role def", "id", id));
        onDeleteInfo(moderatorAuthority, info);
    }

    @Override
    public void onDeleteInfo(ModeratorAuthority moderatorAuthority, Collection<String> info) {
        moderatorAuthority.getCensoredCustomers().forEach(customerRoleDef -> {
            String username = customerRoleDef.getUser().getUsername();
            info.add(username + " will be uncensored when the moderator role def is deleted");
        });
    }

    @Override
    public void setCustomerCensoredStatus(Long moderatorRoleDefId, Long customerRoleDefId, boolean censor)
            throws NoEntityFoundException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Set customer censored status");
        ModeratorAuthority moderatorAuthority = findById(moderatorRoleDefId).orElseThrow(
                () -> new NoEntityFoundException("moderator", "id", moderatorRoleDefId));
        logger.debug("Found moderator by id: " + moderatorAuthority);
        CustomerAuthority customerAuthority = customerAuthorityService.findById(customerRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException("customer", "id", customerRoleDefId));
        logger.debug("Found customer by id: " + customerAuthority);
        logger.debug("Setting censored status to " + censor);
        customerAuthority.setCensoredBy(censor ? moderatorAuthority : null);
        logger.debug("Saved customer: " + customerAuthority);
        customerAuthorityService.save(customerAuthority);
    }

    @Override
    public List<CustomerAuthorityDto> censoredCustomerDtosOfUserWithId(Long userId)
            throws NoEntityFoundException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Find censored customer dtos of user with id " + userId);
        ModeratorAuthority moderatorAuthority = findByUserWithId(userId).orElseThrow(
                () -> new NoEntityFoundException("moderator", "user id", userId));
        logger.debug("Found moderator by id: " + moderatorAuthority);
        Set<CustomerAuthority> censoredCustomers = moderatorAuthority.getCensoredCustomers();
        logger.debug("Found censored customers: " + censoredCustomers);
        return customerAuthorityService.convertToDto(censoredCustomers);
    }

}
