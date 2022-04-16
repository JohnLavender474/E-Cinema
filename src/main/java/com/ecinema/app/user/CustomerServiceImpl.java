package com.ecinema.app.user;

import com.ecinema.app.abstraction.AbstractServiceImpl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerServiceImpl extends AbstractServiceImpl<Customer, CustomerRepository> implements CustomerService {

    public CustomerServiceImpl(CustomerRepository repository) {
        super(repository);
    }

    @Override
    public List<Customer> findAllByIsRewardsMember(Boolean isRewardsMember) {
        return repository.findAllByIsRewardsMember(isRewardsMember);
    }

    @Override
    public List<Customer> findAllByIsReceivingNewsLetters(Boolean isReceivingNewsLetters) {
        return repository.findAllByIsReceivingNewsLetters(isReceivingNewsLetters);
    }

}
