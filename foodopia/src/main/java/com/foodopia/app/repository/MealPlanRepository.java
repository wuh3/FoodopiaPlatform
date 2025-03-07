package com.foodopia.app.repository;

import com.foodopia.app.models.operations.MealPlan;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface MealPlanRepository extends MongoRepository<MealPlan, String> {
    List<MealPlan> findByIsActiveTrue();
}