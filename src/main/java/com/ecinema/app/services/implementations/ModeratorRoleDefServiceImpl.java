package com.ecinema.app.services.implementations;

import com.ecinema.app.domain.entities.CustomerRoleDef;
import com.ecinema.app.domain.entities.ModeratorRoleDef;
import com.ecinema.app.exceptions.ClashException;
import com.ecinema.app.exceptions.NoEntityFoundException;
import com.ecinema.app.repositories.ModeratorRoleDefRepository;
import com.ecinema.app.services.CustomerRoleDefService;
import com.ecinema.app.services.ModeratorRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        for (CustomerRoleDef customerRoleDef : userRoleDef.getCensoredCustomers()) {
            customerRoleDef.setCensoredBy(null);
            userRoleDef.getCensoredCustomers().remove(customerRoleDef);
        }
    }

    @Override
    public void censorCustomerWithId(Long moderatorRoleDefId, Long customerRoleDefId)
            throws NoEntityFoundException, ClashException {
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
        save(moderatorRoleDef);
    }

}
