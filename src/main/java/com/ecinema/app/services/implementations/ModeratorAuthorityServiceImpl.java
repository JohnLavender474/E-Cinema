package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.ModeratorAuthority;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ModeratorAuthorityRepository;
import com.ecinema.app.services.CustomerAuthorityService;
import com.ecinema.app.services.ModeratorAuthorityService;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;

@Service
@Transactional
public class ModeratorAuthorityServiceImpl extends AbstractUserAuthorityServiceImpl<ModeratorAuthority,
        ModeratorAuthorityRepository> implements ModeratorAuthorityService {

    private final CustomerAuthorityService customerAuthorityService;

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
        Iterator<CustomerAuthority> customerRoleDefIterator = AbstractUserAuthority.getCensoredCustomers().iterator();
        while (customerRoleDefIterator.hasNext()) {
            CustomerAuthority customerAuthority = customerRoleDefIterator.next();
            logger.debug("Detaching customer role def: " + customerAuthority);
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
    public void censorCustomerWithId(Long moderatorRoleDefId, Long customerRoleDefId)
            throws NoEntityFoundException, ClashException {
        logger.debug(UtilMethods.getDelimiterLine());
        logger.debug("Moderator with id: " + moderatorRoleDefId);
        logger.debug("Censoring customer with id: " + customerRoleDefId);
        ModeratorAuthority moderatorAuthority = findById(moderatorRoleDefId).orElseThrow(
                () -> new NoEntityFoundException(
                        "moderator role def", "id", moderatorRoleDefId));
        CustomerAuthority customerAuthority = customerAuthorityService.findById(customerRoleDefId)
                                                                      .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "id", customerRoleDefId));
        if (customerAuthority.getCensoredBy() != null) {
            throw new ClashException("This customer has already been censored, action cannot be duplicated");
        }
        customerAuthority.setCensoredBy(moderatorAuthority);
        moderatorAuthority.getCensoredCustomers().add(customerAuthority);
        logger.debug("Customer role def is now censored: " + customerAuthority);
        logger.debug("Moderator role def: " + moderatorAuthority);
        save(moderatorAuthority);
    }

}
