package com.foodopia.app.repository;

import com.foodopia.app.models.users.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CustomerRepository extends MongoRepository<Customer, Long> {
    Optional<Customer> findByUsername(String username);
}