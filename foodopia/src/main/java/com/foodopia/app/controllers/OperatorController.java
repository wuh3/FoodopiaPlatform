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
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/operator")
@PreAuthorize("hasAnyRole('ADMIN', 'OPERATOR')")  // Ensures controller is accessible to both ADMIN and OPERATOR
public class OperatorController {

    private final DishRepository dishRepository;
    private final IngredientRepository ingredientRepository;

    @Autowired
    public OperatorController(DishRepository dishRepository, IngredientRepository ingredientRepository) {
        this.dishRepository = dishRepository;
        this.ingredientRepository = ingredientRepository;
    }

    // Ingredient operations for operators
    @GetMapping("/ingredients")
    public ResponseEntity<List<Ingredient>> getAllIngredients() {
        return ResponseEntity.ok(ingredientRepository.findByIsActiveTrue());
    }

    @GetMapping("/ingredients/{id}")
    public ResponseEntity<Ingredient> getIngredientById(@PathVariable String id) {
        return ingredientRepository.findById(id)
                .filter(Ingredient::isActive)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Operators can update prices but cannot create/delete ingredients
    @PutMapping("/ingredients/{id}/price")
    public ResponseEntity<Ingredient> updateIngredientPrice(
            @PathVariable String id,
            @RequestParam double unitPrice) {

        return ingredientRepository.findById(id)
                .filter(Ingredient::isActive)
                .map(ingredient -> {
                    ingredient.setUnitPrice(unitPrice);
                    ingredient.setUpdatedAt(new Date());
                    return ResponseEntity.ok(ingredientRepository.save(ingredient));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Dish operations for operators
    @GetMapping("/dishes")
    public ResponseEntity<List<Dish>> getAllDishes() {
        return ResponseEntity.ok(dishRepository.findByIsActiveTrue());
    }

    @GetMapping("/dishes/{id}")
    public ResponseEntity<Dish> getDishById(@PathVariable String id) {
        return dishRepository.findById(id)
                .filter(Dish::isActive)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Operators can update preparation costs and markup percentages
    @PutMapping("/dishes/{id}/costs")
    public ResponseEntity<Dish> updateDishCosts(
            @PathVariable String id,
            @RequestParam(required = false) Double preparationCost,
            @RequestParam(required = false) Double markupPercentage) {

        return dishRepository.findById(id)
                .filter(Dish::isActive)
                .map(dish -> {
                    if (preparationCost != null) {
                        dish.setPreparationCost(preparationCost);
                    }
                    if (markupPercentage != null) {
                        dish.setMarkupPercentage(markupPercentage);
                    }
                    dish.setUpdatedAt(new Date());
                    return ResponseEntity.ok(dishRepository.save(dish));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // Endpoints for cost calculation
    @GetMapping("/dishes/{id}/cost")
    public ResponseEntity<Double> calculateDishCost(@PathVariable String id) {
        return dishRepository.findById(id)
                .filter(Dish::isActive)
                .map(dish -> ResponseEntity.ok(dish.calculateCost()))
                .orElse(ResponseEntity.notFound().build());
    }

    // Calculate costs for multiple dishes
    @PostMapping("/dishes/calculate-costs")
    public ResponseEntity<Map<String, Double>> calculateMultipleDishCosts(@RequestBody List<String> dishIds) {
        Map<String, Double> costs = new HashMap<>();

        for (String id : dishIds) {
            dishRepository.findById(id)
                    .filter(Dish::isActive)
                    .ifPresent(dish -> costs.put(id, dish.calculateCost()));
        }

        return ResponseEntity.ok(costs);
    }
}