package com.foodopia.app.models.operations;

import com.foodopia.app.domain.ICostCalculable;
import com.foodopia.app.security.OperatorAccess;

import org.springframework.data.mongodb.core.mapping.DBRef;

public class DishIngredient implements ICostCalculable {
    private String ingredientId;

    @DBRef(lazy = true)
    private Ingredient ingredient;

    private double quantity;
    private String unitMeasure;

    @Override
    public double calculateCost() {
        if (ingredient != null) {
            return ingredient.getUnitPrice() * quantity;
        }
        return 0; // Return 0 if ingredient reference is not loaded
    }

    // Getters - accessible to all users
    public String getIngredientId() {
        return ingredientId;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public double getQuantity() {
        return quantity;
    }

    public String getUnitMeasure() {
        return unitMeasure;
    }

    // Setters - accessible only to ADMIN and OPERATOR roles
    @OperatorAccess
    public void setIngredientId(String ingredientId) {
        this.ingredientId = ingredientId;
    }

    @OperatorAccess
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
        if (ingredient != null) {
            this.ingredientId = ingredient.getId();
        }
    }

    @OperatorAccess
    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    @OperatorAccess
    public void setUnitMeasure(String unitMeasure) {
        this.unitMeasure = unitMeasure;
    }
}