package com.foodopia.app.repository;

import com.foodopia.app.models.operations.Meal;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
import java.util.Date;

public interface MealRepository extends MongoRepository<Meal, String> {
    List<Meal> findByCustomerId(String customerId);
    List<Meal> findByCustomerIdAndScheduledDeliveryDateBetween(String customerId, Date start, Date end);
    List<Meal> findByStatus(Meal.MealStatus status);
}