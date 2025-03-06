package com.foodopia.app.models.operations;

import com.foodopia.app.domain.ICostCalculable;
import com.foodopia.app.security.OperatorAccess;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "dishes")
public class Dish implements ICostCalculable {
    @Id
    private String id;
    private String name;
    private String description;
    private String imageUrl;
    private List<String> tags;
    private List<DishIngredient> ingredients;
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    // Cost calculation fields
    private double preparationCost;
    private double markupPercentage;

    @Override
    public double calculateCost() {
        double ingredientsCost = 0;

        if (ingredients != null) {
            for (DishIngredient dishIngredient : ingredients) {
                ingredientsCost += dishIngredient.calculateCost();
            }
        }

        double baseCost = ingredientsCost + preparationCost;
        return baseCost * (1 + markupPercentage / 100);
    }

    // Getters - accessible to all users
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<String> getTags() {
        return tags;
    }

    public List<DishIngredient> getIngredients() {
        return ingredients;
    }

    public boolean isActive() {
        return isActive;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public double getPreparationCost() {
        return preparationCost;
    }

    public double getMarkupPercentage() {
        return markupPercentage;
    }

    // Setters - accessible only to ADMIN and OPERATOR roles
    @OperatorAccess
    public void setId(String id) {
        this.id = id;
    }

    @OperatorAccess
    public void setName(String name) {
        this.name = name;
    }

    @OperatorAccess
    public void setDescription(String description) {
        this.description = description;
    }

    @OperatorAccess
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @OperatorAccess
    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    @OperatorAccess
    public void setIngredients(List<DishIngredient> ingredients) {
        this.ingredients = ingredients;
    }

    @OperatorAccess
    public void setActive(boolean active) {
        this.isActive = active;
    }

    @OperatorAccess
    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @OperatorAccess
    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    @OperatorAccess
    public void setPreparationCost(double preparationCost) {
        this.preparationCost = preparationCost;
    }

    @OperatorAccess
    public void setMarkupPercentage(double markupPercentage) {
        this.markupPercentage = markupPercentage;
    }
}