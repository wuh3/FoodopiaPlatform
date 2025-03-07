package com.foodopia.app.models.operations;

import com.foodopia.app.domain.ICostCalculable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;
import java.util.Date;

@Document(collection = "meals")
public class Meal implements ICostCalculable {
    @Id
    private String id;
    private String customerId;
    private String subscriptionId;
    private List<String> dishIds;

    @DBRef(lazy = true)
    private List<Dish> dishes;

    private int numberOfDishes;
    private Date scheduledDeliveryDate;
    private String deliveryAddress;
    private String deliveryInstructions;
    private MealStatus status;
    private Date createdAt;
    private Date updatedAt;

    // Cost calculation fields
    private double packagingCost;
    private double deliveryCost;

    public enum MealStatus {
        SCHEDULED,
        PREPARING,
        OUT_FOR_DELIVERY,
        DELIVERED,
        CANCELLED,
        COMPLETED,
        RATED
    }

    @Override
    public double calculateCost() {
        double dishesCost = 0;

        if (dishes != null) {
            for (Dish dish : dishes) {
                dishesCost += dish.calculateCost();
            }
        }

        return dishesCost + packagingCost + deliveryCost;
    }

    // Standard getters and setters
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

    public String getSubscriptionId() {
        return subscriptionId;
    }

    public void setSubscriptionId(String subscriptionId) {
        this.subscriptionId = subscriptionId;
    }

    public List<String> getDishIds() {
        return dishIds;
    }

    public void setDishIds(List<String> dishIds) {
        this.dishIds = dishIds;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
        if (dishes != null) {
            this.dishIds = dishes.stream()
                    .map(Dish::getId)
                    .toList();
        }
    }

    public int getNumberOfDishes() {
        return numberOfDishes;
    }

    public void setNumberOfDishes(int numberOfDishes) {
        this.numberOfDishes = numberOfDishes;
    }

    public Date getScheduledDeliveryDate() {
        return scheduledDeliveryDate;
    }

    public void setScheduledDeliveryDate(Date scheduledDeliveryDate) {
        this.scheduledDeliveryDate = scheduledDeliveryDate;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getDeliveryInstructions() {
        return deliveryInstructions;
    }

    public void setDeliveryInstructions(String deliveryInstructions) {
        this.deliveryInstructions = deliveryInstructions;
    }

    public MealStatus getStatus() {
        return status;
    }

    public void setStatus(MealStatus status) {
        this.status = status;
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

    public double getPackagingCost() {
        return packagingCost;
    }

    public void setPackagingCost(double packagingCost) {
        this.packagingCost = packagingCost;
    }

    public double getDeliveryCost() {
        return deliveryCost;
    }

    public void setDeliveryCost(double deliveryCost) {
        this.deliveryCost = deliveryCost;
    }
}