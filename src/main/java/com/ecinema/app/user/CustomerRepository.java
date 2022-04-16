package com.ecinema.app.user;

import com.ecinema.app.user.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    List<Customer> findAllByIsRewardsMember(Boolean isRewardsMember);
    List<Customer> findAllByIsReceivingNewsLetters(Boolean isReceivingNewsLetters);
}
