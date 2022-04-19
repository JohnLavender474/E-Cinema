package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long> {

    List<PaymentCard> findAllByCustomerRoleDef(CustomerRoleDef customerRoleDef);

    @Query("SELECT p FROM PaymentCard p WHERE p.customerRoleDef.id = ?1 ORDER BY p.cardNumber")
    List<PaymentCard> findAllByCustomerRoleDefWithId(Long customerAuthId);

}
