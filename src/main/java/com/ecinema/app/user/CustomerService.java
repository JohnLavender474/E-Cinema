package com.ecinema.app.user;

import com.ecinema.app.abstraction.AbstractService;

import java.util.List;

public interface CustomerService extends AbstractService<Customer> {
    List<Customer> findAllByIsRewardsMember(Boolean isRewardsMember);
    List<Customer> findAllByIsReceivingNewsLetters(Boolean isReceivingNewsLetters);
}
