package com.foodopia.app.controllers;

import com.foodopia.app.models.operations.Dish;
import com.foodopia.app.models.operations.Ingredient;
import com.foodopia.app.repository.DishRepository;
import com.foodopia.app.repository.IngredientRepository;
import com.foodopia.app.security.OperatorAccess;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")  // Ensures entire controller is only accessible to ADMINs
public class AdminController {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public AdminController(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // Ingredient management endpoints
    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientRepository.findAll());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable String id) {
        return ingredientRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/ingredients")
    public ResponseEntity<Ingredient> createIngredient(@RequestBody Ingredient ingredient) {
        ingredient.setCreatedAt(new Date());
        ingredient.setUpdatedAt(new Date());
        ingredient.setActive(true);
        return ResponseEntity.ok(ingredientRepository.save(ingredient));
    }

    @PutMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> updateIngredient(@PathVariable String id, @RequestBody Ingredient ingredient) {
        return ingredientRepository.findById(id)
                .map(existingIngredient -> {
                    existingIngredient.setName(ingredient.getName());
                    existingIngredient.setCategory(ingredient.getCategory());
                    existingIngredient.setUnitPrice(ingredient.getUnitPrice());
                    existingIngredient.setUnitMeasure(ingredient.getUnitMeasure());
                    existingIngredient.setUpdatedAt(new Date());
                    return ResponseEntity.ok(ingredientRepository.save(existingIngredient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/ingredients/{id}")
    public ResponseEntity<Void> deleteIngredient(@PathVariable String id) {
        return ingredientRepository.findById(id)
                .map(ingredient -> {
                    ingredient.setActive(false);
                    ingredient.setUpdatedAt(new Date());
                    ingredientRepository.save(ingredient);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Dish management endpoints
    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishRepository.findAll());
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable String id) {
        return dishRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/dishes")
    public ResponseEntity<Dish> createDish(@RequestBody Dish dish) {
        dish.setCreatedAt(new Date());
        dish.setUpdatedAt(new Date());
        dish.setActive(true);
        return ResponseEntity.ok(dishRepository.save(dish));
    }

    @PutMapping("/dishes/{id}")
    public ResponseEntity<Dish> updateDish(@PathVariable String id, @RequestBody Dish dish) {
        return dishRepository.findById(id)
                .map(existingDish -> {
                    existingDish.setName(dish.getName());
                    existingDish.setDescription(dish.getDescription());
                    existingDish.setImageUrl(dish.getImageUrl());
                    existingDish.setTags(dish.getTags());
                    existingDish.setIngredients(dish.getIngredients());
                    existingDish.setPreparationCost(dish.getPreparationCost());
                    existingDish.setMarkupPercentage(dish.getMarkupPercentage());
                    existingDish.setUpdatedAt(new Date());
                    return ResponseEntity.ok(dishRepository.save(existingDish));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/dishes/{id}")
    public ResponseEntity<Void> deleteDish(@PathVariable String id) {
        return dishRepository.findById(id)
                .map(dish -> {
                    dish.setActive(false);
                    dish.setUpdatedAt(new Date());
                    dishRepository.save(dish);
                    return ResponseEntity.noContent().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}