package com.ecinema.app.repositories;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.Review;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ModeratorRoleDefRepository extends UserRoleDefRepository<ModeratorRoleDef> {

    @Query("SELECT m FROM ModeratorRoleDef m WHERE size(m.censoredReviews) > 0 ORDER BY m.user.lastName")
    List<ModeratorRoleDef> findAllByContainsCensoredReviews();

    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredReviews c WHERE c = ?1")
    Optional<ModeratorRoleDef> findByCensoredReviewsContains(Review review);

    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredReviews c WHERE c.id = ?1")
    Optional<ModeratorRoleDef> findByCensoredReviewsContainsWithId(Long censoredReviewId);

    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredCustomers c WHERE c = ?1")
    Optional<ModeratorRoleDef> findByCensoredUsersContains(CustomerRoleDef customerRoleDef);

    @Query("SELECT m FROM ModeratorRoleDef m JOIN m.censoredCustomers c WHERE c.id = ?1")
    Optional<ModeratorRoleDef> findByCensoredUsersContainsWithId(Long censoredCustomerId);

}
