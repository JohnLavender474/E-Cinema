package com.ecinema.app.services.implementations;

import com.ecinema.app.entities.CustomerRoleDef;
import com.ecinema.app.entities.PaymentCard;
import com.ecinema.app.entities.Review;
import com.ecinema.app.entities.Ticket;
import com.ecinema.app.repositories.CustomerRoleDefRepository;
import com.ecinema.app.services.CustomerRoleDefService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class CustomerRoleDefServiceImpl extends UserRoleDefServiceImpl<CustomerRoleDef,
        CustomerRoleDefRepository> implements CustomerRoleDefService {

    public CustomerRoleDefServiceImpl(CustomerRoleDefRepository repository) {
        super(repository);
    }

    @Override
    protected void onDelete(CustomerRoleDef customerRoleDef) {

    }

    @Override
    public Optional<CustomerRoleDef> findByPaymentCardsContains(PaymentCard paymentCard) {
        return repository.findByPaymentCardsContains(paymentCard);
    }

    @Override
    public Optional<CustomerRoleDef> findByTicketsContains(Ticket ticket) {
        return repository.findByTicketsContains(ticket);
    }

    @Override
    public Optional<CustomerRoleDef> findByReviewsContains(Review review) {
        return repository.findByReviewsContains(review);
    }

}
