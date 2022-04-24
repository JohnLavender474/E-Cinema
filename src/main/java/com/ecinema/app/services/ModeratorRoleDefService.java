package com.ecinema.app.services;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.Review;

import java.util.List;
import java.util.Optional;

/**
 * The interface Moderator role def service.
 */
public interface ModeratorRoleDefService extends UserRoleDefService<ModeratorRoleDef> {

    /**
     * Find all by contains censored reviews list.
     *
     * @return the list
     */
    List<ModeratorRoleDef> findAllByContainsCensoredReviews();

    /**
     * Find by censored reviews contains optional.
     *
     * @param review the review
     * @return the optional
     */
    Optional<ModeratorRoleDef> findByCensoredReviewsContains(Review review);

    /**
     * Find by censored reviews contains with id optional.
     *
     * @param censoredReviewId the censored review id
     * @return the optional
     */
    Optional<ModeratorRoleDef> findByCensoredReviewsContainsWithId(Long censoredReviewId);

    /**
     * Find by censored users contains optional.
     *
     * @param customerRoleDef the customer role def
     * @return the optional
     */
    Optional<ModeratorRoleDef> findByCensoredUsersContains(CustomerRoleDef customerRoleDef);

    /**
     * Find by censored users contains with id optional.
     *
     * @param censoredCustomerId the censored customer id
     * @return the optional
     */
    Optional<ModeratorRoleDef> findByCensoredUsersContainsWithId(Long censoredCustomerId);

}
