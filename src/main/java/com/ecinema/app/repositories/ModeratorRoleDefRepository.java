package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * The interface Moderator role def repository.
 */
@Repository
public interface ModeratorRoleDefRepository extends UserRoleDefRepository<ModeratorRoleDef> {

    /**
     * Find all by contains censored reviews list.
     *
     * @return the list
     */
    @Query("SELECT m FROM ModeratorRoleDef m WHERE size(m.censoredReviews) > 0 ORDER BY m.user.lastName")
    List<ModeratorRoleDef> findAllByContainsCensoredReviews();

    /**
     * Find by censored reviews contains optional.
     *
     * @param review the review
     * @return the optional
     */
    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredReviews c WHERE c = ?1")
    Optional<ModeratorRoleDef> findByCensoredReviewsContains(Review review);

    /**
     * Find by censored reviews contains with id optional.
     *
     * @param censoredReviewId the censored review id
     * @return the optional
     */
    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredReviews c WHERE c.id = ?1")
    Optional<ModeratorRoleDef> findByCensoredReviewsContainsWithId(Long censoredReviewId);

    /**
     * Find by censored users contains optional.
     *
     * @param customerRoleDef the customer role def
     * @return the optional
     */
    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredCustomers c WHERE c = ?1")
    Optional<ModeratorRoleDef> findByCensoredUsersContains(CustomerRoleDef customerRoleDef);

    /**
     * Find by censored users contains with id optional.
     *
     * @param censoredCustomerId the censored customer id
     * @return the optional
     */
    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredCustomers c WHERE c.id = ?1")
    Optional<ModeratorRoleDef> findByCensoredUsersContainsWithId(Long censoredCustomerId);

}
