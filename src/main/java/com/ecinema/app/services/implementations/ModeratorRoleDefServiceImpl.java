package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.ModeratorRoleDef;
import com.ecinema.app.entities.Review;
import com.ecinema.app.repositories.ModeratorRoleDefRepository;
import com.ecinema.app.services.ModeratorRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ModeratorRoleDefServiceImpl extends UserRoleDefServiceImpl<ModeratorRoleDef,
        ModeratorRoleDefRepository> implements ModeratorRoleDefService {

    public ModeratorRoleDefServiceImpl(ModeratorRoleDefRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(ModeratorRoleDef moderatorRoleDef) {

    }

    @Override
    public List<ModeratorRoleDef> findAllByContainsCensoredReviews() {
        return repository.findAllByContainsCensoredReviews();
    }

    @Override
    public Optional<ModeratorRoleDef> findByCensoredReviewsContains(Review review) {
        return repository.findByCensoredReviewsContains(review);
    }

    @Override
    public Optional<ModeratorRoleDef> findByCensoredReviewsContainsWithId(Long censoredReviewId) {
        return repository.findByCensoredReviewsContainsWithId(censoredReviewId);
    }

    @Override
    public Optional<ModeratorRoleDef> findByCensoredUsersContains(CustomerRoleDef customerRoleDef) {
        return repository.findByCensoredUsersContains(customerRoleDef);
    }

    @Override
    public Optional<ModeratorRoleDef> findByCensoredUsersContainsWithId(Long censoredCustomerId) {
        return repository.findByCensoredUsersContainsWithId(censoredCustomerId);
    }

}
