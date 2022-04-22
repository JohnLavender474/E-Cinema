package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Payment card repository.
 */
@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    /**
     * Find distinct by customer role def list.
     *
     * @param customerRoleDef the customer role def
     * @return the list
     */
    @Query("SELECT p FROM PaymentCard p JOIN p.customerRoleDef c WHERE c = ?1")
    List<PaymentCard> findDistinctByCustomerRoleDef(CustomerRoleDef customerRoleDef);

    /**
     * Find distinct by customer role def id list.
     *
     * @param customerRoleDefId the customer role def id
     * @return the list
     */
    @Query("SELECT p FROM PaymentCard p JOIN p.customerRoleDef c WHERE c.id = ?1")
    List<PaymentCard> findDistinctByCustomerRoleDefWithId(Long customerRoleDefId);

}
