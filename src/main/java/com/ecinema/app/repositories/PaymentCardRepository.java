package com.ecinema.app.repositories;

import com.ecinema.app.domain.entities.CustomerAuthority;
import com.ecinema.app.domain.entities.PaymentCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * The interface Payment card repository.
 */
@Repository
public interface PaymentCardRepository extends JpaRepository<PaymentCard, Long>, AbstractRepository {

    /**
     * Find distinct by customer role def list.
     *
     * @param customerAuthority the customer role def
     * @return the list
     */
    @Query("SELECT p FROM PaymentCard p JOIN p.cardOwner c WHERE c = ?1")
    List<PaymentCard> findDistinctByCardOwner(CustomerAuthority customerAuthority);

    /**
     * Find distinct by customer role def id list.
     *
     * @param customerAuthorityId the customer role def id
     * @return the list
     */
    @Query("SELECT p FROM PaymentCard p JOIN p.cardOwner c WHERE c.id = ?1")
    List<PaymentCard> findDistinctByCardOwnerWithId(Long customerAuthorityId);

}
