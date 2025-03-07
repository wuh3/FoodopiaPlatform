package com.foodopia.app.repository;

import com.foodopia.app.models.users.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends MongoRepository<Subscription, String> {
    List<Subscription> findByCustomerId(String customerId);
    List<Subscription> findByStatus(Subscription.SubscriptionStatus status);
    Optional<Subscription> findByCustomerIdAndStatus(String customerId, Subscription.SubscriptionStatus status);
}