package com.foodopia.app.models.users;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;

@Document(collection = "ratings")
@CompoundIndexes({
        @CompoundIndex(name = "user_meal", def = "{'customerId': 1, 'mealId': 1}", unique = true),
        @CompoundIndex(name = "meal_date", def = "{'mealId': 1, 'createdAt': 1}")
})
public class Rating {
    @Id
    private String id;

    // Who created the rating
    private String customerId;

    // The rated meal
    private String mealId;

    // Overall meal rating
    private Double mealRating;

    // Individual dish ratings (dishId -> rating)
    private Map<String, Double> dishRatings = new HashMap<>();

    // Optional feedback
    private String feedback;

    // Timestamps
    private Date createdAt;
    private Date updatedAt;

    // Getters and setters
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

    public String getMealId() {
        return mealId;
    }

    public void setMealId(String mealId) {
        this.mealId = mealId;
    }

    public Double getMealRating() {
        return mealRating;
    }

    public void setMealRating(Double mealRating) {
        this.mealRating = mealRating;
    }

    public Map<String, Double> getDishRatings() {
        return dishRatings;
    }

    public void setDishRatings(Map<String, Double> dishRatings) {
        this.dishRatings = dishRatings;
    }

    public void addDishRating(String dishId, Double rating) {
        if (this.dishRatings == null) {
            this.dishRatings = new HashMap<>();
        }
        this.dishRatings.put(dishId, rating);
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
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
}