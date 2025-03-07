package com.foodopia.app.repository;

import com.foodopia.app.models.operations.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    List<Ingredient> findByIsActiveTrue();
    List<Ingredient> findByCategory(String category);
}