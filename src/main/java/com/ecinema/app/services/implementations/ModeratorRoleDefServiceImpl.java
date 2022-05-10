package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.ModeratorRoleDef;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ModeratorRoleDefRepository;
import com.ecinema.app.services.CustomerRoleDefService;
import com.ecinema.app.services.ModeratorRoleDefService;
import com.ecinema.app.utils.UtilMethods;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Iterator;

@Service
@Transactional
public class ModeratorRoleDefServiceImpl extends UserRoleDefServiceImpl<ModeratorRoleDef,
        ModeratorRoleDefRepository> implements ModeratorRoleDefService {

    private final CustomerRoleDefService customerRoleDefService;

    public ModeratorRoleDefServiceImpl(ModeratorRoleDefRepository repository,
                                       CustomerRoleDefService customerRoleDefService) {
        super(repository);
        this.customerRoleDefService = customerRoleDefService;
    }

    @Override
    protected void onDelete(ModeratorRoleDef userRoleDef) {
        // detach User
        super.onDelete(userRoleDef);
        // uncensor and detach censored Customers
        Iterator<CustomerRoleDef> customerRoleDefIterator = userRoleDef.getCensoredCustomers().iterator();
        while (customerRoleDefIterator.hasNext()) {
            CustomerRoleDef customerRoleDef = customerRoleDefIterator.next();
            logger.debug("Detaching customer role def: " + customerRoleDef);
            customerRoleDef.setCensoredBy(null);
            customerRoleDefIterator.remove();
        }
    }

    @Override
    public void onDeleteInfo(Long id, Collection<String> info)
            throws NoEntityFoundException {
        ModeratorRoleDef moderatorRoleDef = findById(id).orElseThrow(
                () -> new NoEntityFoundException("moderator role def", "id", id));
        onDeleteInfo(moderatorRoleDef, info);
    }

    @Override
    public void onDeleteInfo(ModeratorRoleDef moderatorRoleDef, Collection<String> info) {
        moderatorRoleDef.getCensoredCustomers().forEach(customerRoleDef -> {
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
        ModeratorRoleDef moderatorRoleDef = findById(moderatorRoleDefId).orElseThrow(
                () -> new NoEntityFoundException(
                        "moderator role def", "id", moderatorRoleDefId));
        CustomerRoleDef customerRoleDef = customerRoleDefService.findById(customerRoleDefId)
                .orElseThrow(() -> new NoEntityFoundException(
                        "customer role def", "id", customerRoleDefId));
        if (customerRoleDef.getCensoredBy() != null) {
            throw new ClashException("This customer has already been censored, action cannot be duplicated");
        }
        customerRoleDef.setCensoredBy(moderatorRoleDef);
        moderatorRoleDef.getCensoredCustomers().add(customerRoleDef);
        logger.debug("Customer role def is now censored: " + customerRoleDef);
        logger.debug("Moderator role def: " + moderatorRoleDef);
        save(moderatorRoleDef);
    }

}
