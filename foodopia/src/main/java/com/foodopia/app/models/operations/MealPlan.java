package com.foodopia.app.models.operations;

import com.foodopia.app.domain.ICostCalculable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "mealPlans")
public class MealPlan implements ICostCalculable {
    @Id
    private String id;
    private String name;
    private int mealsPerMonth;
    private double price; // Customer-facing price
    private String description;
    private List<Integer> dishOptions; // Number of dishes per meal options
    private boolean isActive;
    private Date createdAt;
    private Date updatedAt;

    // Cost calculation fields
    private double baseCostPerMeal; // Average cost of a meal in this plan
    private double discountPercentage; // Discount applied for the subscription

    @Override
    public double calculateCost() {
        // Calculate the base cost of the meal plan (without discount)
        double totalBaseCost = baseCostPerMeal * mealsPerMonth;

        // Apply discount
        return totalBaseCost * (1 - discountPercentage / 100);
    }

    // Method to calculate the profit margin
    public double calculateProfitMargin() {
        double cost = calculateCost();
        if (cost > 0) {
            return ((price - cost) / price) * 100;
        }
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMealsPerMonth() {
        return mealsPerMonth;
    }

    public void setMealsPerMonth(int mealsPerMonth) {
        this.mealsPerMonth = mealsPerMonth;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Integer> getDishOptions() {
        return dishOptions;
    }

    public void setDishOptions(List<Integer> dishOptions) {
        this.dishOptions = dishOptions;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public double getBaseCostPerMeal() {
        return baseCostPerMeal;
    }

    public void setBaseCostPerMeal(double baseCostPerMeal) {
        this.baseCostPerMeal = baseCostPerMeal;
    }

    public double getDiscountPercentage() {
        return discountPercentage;
    }

    public void setDiscountPercentage(double discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
}