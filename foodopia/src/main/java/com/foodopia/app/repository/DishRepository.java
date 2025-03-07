package com.foodopia.app.repository;

import com.foodopia.app.models.operations.Dish;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface DishRepository extends MongoRepository<Dish, String> {
    List<Dish> findByIsActiveTrue();
    List<Dish> findByTagsContaining(String tag);
    List<Dish> findByNameContainingIgnoreCase(String keyword);
}