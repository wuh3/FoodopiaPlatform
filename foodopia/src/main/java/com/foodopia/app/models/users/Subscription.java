package com.foodopia.app.models.users;

import com.foodopia.app.domain.ICostCalculable;
import com.foodopia.app.models.operations.Meal;
import com.foodopia.app.models.operations.MealPlan;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "subscriptions")
public class Subscription implements ICostCalculable {
    @Id
    private String id;
    private String customerId;
    private String mealPlanId;

    @DBRef(lazy = true)
    private MealPlan mealPlan;

    private int dishesPerMeal;
    private Date startDate;
    private Date endDate;
    private SubscriptionStatus status;
    private double monthlyPrice; // Price charged to customer
    private String paymentMethod;
    private List<Date> deliveryDates;
    private boolean autoRenew;
    private Date lastRenewalDate;
    private Date nextRenewalDate;
    private Date createdAt;
    private Date updatedAt;

    // List of meal IDs associated with this subscription
    private List<String> mealIds;

    @DBRef(lazy = true)
    private List<Meal> meals; // Lazy-loaded meals

    public enum SubscriptionStatus {
        ACTIVE,
        PAUSED,
        CANCELLED,
        EXPIRED
    }

    @Override
    public double calculateCost() {
        // If mealPlan is loaded, use its cost calculation
        if (mealPlan != null) {
            return mealPlan.calculateCost();
        }
        // If meals are loaded, calculate from meals
        else if (meals != null && !meals.isEmpty()) {
            double totalMealCost = 0;
            for (Meal meal : meals) {
                totalMealCost += meal.calculateCost();
            }
            return totalMealCost;
        }

        // Fall back to a simple calculation if neither is loaded
        return 0;
    }

    // Calculate profit for this subscription
    public double calculateProfit() {
        return monthlyPrice - calculateCost();
    }

    // Calculate profit margin percentage
    public double calculateProfitMargin() {
        if (monthlyPrice > 0) {
            return (calculateProfit() / monthlyPrice) * 100;
        }
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getMealPlanId() {
        return mealPlanId;
    }

    public void setMealPlanId(String mealPlanId) {
        this.mealPlanId = mealPlanId;
    }

    public MealPlan getMealPlan() {
        return mealPlan;
    }

    public void setMealPlan(MealPlan mealPlan) {
        this.mealPlan = mealPlan;
        if (mealPlan != null) {
            this.mealPlanId = mealPlan.getId();
        }
    }

    public int getDishesPerMeal() {
        return dishesPerMeal;
    }

    public void setDishesPerMeal(int dishesPerMeal) {
        this.dishesPerMeal = dishesPerMeal;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public SubscriptionStatus getStatus() {
        return status;
    }

    public void setStatus(SubscriptionStatus status) {
        this.status = status;
    }

    public double getMonthlyPrice() {
        return monthlyPrice;
    }

    public void setMonthlyPrice(double monthlyPrice) {
        this.monthlyPrice = monthlyPrice;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<Date> getDeliveryDates() {
        return deliveryDates;
    }

    public void setDeliveryDates(List<Date> deliveryDates) {
        this.deliveryDates = deliveryDates;
    }

    public boolean isAutoRenew() {
        return autoRenew;
    }

    public void setAutoRenew(boolean autoRenew) {
        this.autoRenew = autoRenew;
    }

    public Date getLastRenewalDate() {
        return lastRenewalDate;
    }

    public void setLastRenewalDate(Date lastRenewalDate) {
        this.lastRenewalDate = lastRenewalDate;
    }

    public Date getNextRenewalDate() {
        return nextRenewalDate;
    }

    public void setNextRenewalDate(Date nextRenewalDate) {
        this.nextRenewalDate = nextRenewalDate;
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

    public List<String> getMealIds() {
        return mealIds;
    }

    public void setMealIds(List<String> mealIds) {
        this.mealIds = mealIds;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
        // Update mealIds
        if (meals != null) {
            this.mealIds = meals.stream()
                    .map(Meal::getId)
                    .toList();
        }
    }
}