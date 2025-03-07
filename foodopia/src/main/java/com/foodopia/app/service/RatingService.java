package com.foodopia.app.service;

import com.foodopia.app.models.operations.Dish;
import com.foodopia.app.models.operations.Meal;
import com.foodopia.app.models.users.Rating;
import com.foodopia.app.repository.DishRepository;
import com.foodopia.app.repository.MealRepository;
import com.foodopia.app.repository.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RatingService {

    private final RatingRepository ratingRepository;
    private final MealRepository mealRepository;
    private final DishRepository dishRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository, MealRepository mealRepository, DishRepository dishRepository) {
        this.ratingRepository = ratingRepository;
        this.mealRepository = mealRepository;
        this.dishRepository = dishRepository;
    }

    /**
     * Create a new rating for a meal
     */
    @Transactional
    public Rating rateMeal(String customerId, String mealId, Double mealRating, Map<String, Double> dishRatings, String feedback) {
        // Validate inputs
        if (mealRating != null && (mealRating < 1 || mealRating > 5)) {
            throw new IllegalArgumentException("Meal rating must be between 1 and 5");
        }

        if (dishRatings != null) {
            for (Double rating : dishRatings.values()) {
                if (rating < 1 || rating > 5) {
                    throw new IllegalArgumentException("Dish ratings must be between 1 and 5");
                }
            }
        }

        // Check if the meal exists and is in the correct state
        Meal meal = mealRepository.findById(mealId)
                .orElseThrow(() -> new IllegalArgumentException("Meal not found"));

        if (meal.getStatus() != Meal.MealStatus.DELIVERED && meal.getStatus() != Meal.MealStatus.COMPLETED) {
            throw new IllegalStateException("Meal must be delivered before it can be rated");
        }

        // Check if the customer owns the meal
        if (!meal.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("Customer does not own this meal");
        }

        // Check if a rating already exists
        Optional<Rating> existingRating = ratingRepository.findByCustomerIdAndMealId(customerId, mealId);
        if (existingRating.isPresent()) {
            throw new IllegalStateException("Meal has already been rated by this customer");
        }

        // Validate that dish IDs in ratings match dish IDs in meal
        if (dishRatings != null && !dishRatings.isEmpty()) {
            for (String dishId : dishRatings.keySet()) {
                if (!meal.getDishIds().contains(dishId)) {
                    throw new IllegalArgumentException("Dish ID " + dishId + " is not part of this meal");
                }
            }
        }

        // Create new rating
        Rating rating = new Rating();
        rating.setCustomerId(customerId);
        rating.setMealId(mealId);
        rating.setMealRating(mealRating);
        rating.setDishRatings(dishRatings != null ? dishRatings : new HashMap<>());
        rating.setFeedback(feedback);
        rating.setCreatedAt(new Date());
        rating.setUpdatedAt(new Date());

        // Save rating
        Rating savedRating = ratingRepository.save(rating);

        // Update meal status
        meal.setStatus(Meal.MealStatus.RATED);
        meal.setUpdatedAt(new Date());
        mealRepository.save(meal);

        return savedRating;
    }

    /**
     * Update an existing rating
     */
    @Transactional
    public Rating updateRating(String customerId, String ratingId, Double mealRating, Map<String, Double> dishRatings, String feedback) {
        // Validate rating values
        if (mealRating != null && (mealRating < 1 || mealRating > 5)) {
            throw new IllegalArgumentException("Meal rating must be between 1 and 5");
        }

        if (dishRatings != null) {
            for (Double rating : dishRatings.values()) {
                if (rating < 1 || rating > 5) {
                    throw new IllegalArgumentException("Dish ratings must be between 1 and 5");
                }
            }
        }

        // Find the existing rating
        Rating rating = ratingRepository.findById(ratingId)
                .orElseThrow(() -> new IllegalArgumentException("Rating not found"));

        // Check if the customer owns the rating
        if (!rating.getCustomerId().equals(customerId)) {
            throw new IllegalArgumentException("Customer does not own this rating");
        }

        // Find the meal to validate dish IDs
        Meal meal = mealRepository.findById(rating.getMealId())
                .orElseThrow(() -> new IllegalArgumentException("Associated meal not found"));

        // Validate that dish IDs in ratings match dish IDs in meal
        if (dishRatings != null && !dishRatings.isEmpty()) {
            for (String dishId : dishRatings.keySet()) {
                if (!meal.getDishIds().contains(dishId)) {
                    throw new IllegalArgumentException("Dish ID " + dishId + " is not part of this meal");
                }
            }
        }

        // Update rating fields
        if (mealRating != null) {
            rating.setMealRating(mealRating);
        }

        if (dishRatings != null) {
            // Merge new dish ratings with existing ones
            Map<String, Double> updatedDishRatings = new HashMap<>(rating.getDishRatings());
            updatedDishRatings.putAll(dishRatings);
            rating.setDishRatings(updatedDishRatings);
        }

        if (feedback != null) {
            rating.setFeedback(feedback);
        }

        rating.setUpdatedAt(new Date());

        // Save updated rating
        return ratingRepository.save(rating);
    }

    /**
     * Get average rating for a dish
     */
    public Double getAverageDishRating(String dishId) {
        return ratingRepository.findAverageRatingForDish(dishId);
    }

    /**
     * Get all ratings for a dish
     */
    public List<Rating> getRatingsForDish(String dishId) {
        return ratingRepository.findByDishRated(dishId);
    }

    /**
     * Get count of ratings for a dish
     */
    public Long getDishRatingCount(String dishId) {
        return ratingRepository.countRatingsForDish(dishId);
    }

    /**
     * Get popular dishes based on their average ratings
     */
    public List<Map<String, Object>> getPopularDishes(int limit) {
        // Get all active dishes
        List<Dish> activeDishes = dishRepository.findByIsActiveTrue();

        // Calculate average rating and count for each dish
        List<Map<String, Object>> dishPopularity = new ArrayList<>();

        for (Dish dish : activeDishes) {
            Double avgRating = ratingRepository.findAverageRatingForDish(dish.getId());
            Long ratingCount = ratingRepository.countRatingsForDish(dish.getId());

            // Skip dishes with no ratings
            if (avgRating == null || ratingCount == 0) {
                continue;
            }

            Map<String, Object> dishData = new HashMap<>();
            dishData.put("dish", dish);
            dishData.put("averageRating", avgRating);
            dishData.put("ratingCount", ratingCount);
            dishData.put("popularityScore", calculatePopularityScore(avgRating, ratingCount));

            dishPopularity.add(dishData);
        }

        // Sort by popularity score (descending)
        dishPopularity.sort((a, b) ->
                Double.compare((Double)b.get("popularityScore"), (Double)a.get("popularityScore")));

        // Limit the results
        if (dishPopularity.size() > limit) {
            dishPopularity = dishPopularity.subList(0, limit);
        }

        return dishPopularity;
    }

    /**
     * Get popular dishes from the previous month
     */
    public List<Map<String, Object>> getPreviousMonthPopularDishes(int limit) {
        // Calculate date range for previous month
        Calendar cal = Calendar.getInstance();
        Date endDate = cal.getTime();

        cal.add(Calendar.MONTH, -1);
        Date startDate = cal.getTime();

        // Get all active dishes
        List<Dish> activeDishes = dishRepository.findByIsActiveTrue();

        // Calculate popularity for each dish based on ratings in the previous month
        List<Map<String, Object>> dishPopularity = new ArrayList<>();

        for (Dish dish : activeDishes) {
            List<Rating> ratings = ratingRepository.findDishRatingsInPeriod(dish.getId(), startDate, endDate);

            if (ratings.isEmpty()) {
                continue;
            }

            // Calculate average rating
            double totalRating = 0;
            for (Rating rating : ratings) {
                totalRating += rating.getDishRatings().get(dish.getId());
            }
            double avgRating = totalRating / ratings.size();

            Map<String, Object> dishData = new HashMap<>();
            dishData.put("dish", dish);
            dishData.put("averageRating", avgRating);
            dishData.put("ratingCount", ratings.size());
            dishData.put("popularityScore", calculatePopularityScore(avgRating, ratings.size()));

            dishPopularity.add(dishData);
        }

        // Sort by popularity score (descending)
        dishPopularity.sort((a, b) ->
                Double.compare((Double)b.get("popularityScore"), (Double)a.get("popularityScore")));

        // Limit the results
        if (dishPopularity.size() > limit) {
            dishPopularity = dishPopularity.subList(0, limit);
        }

        return dishPopularity;
    }

    /**
     * Calculate a popularity score based on rating and count
     * This formula can be adjusted based on business needs
     */
    private double calculatePopularityScore(double rating, long count) {
        // Simple formula: rating * log(count + 1)
        // This gives more weight to rating but still considers number of ratings
        return rating * Math.log10(count + 1);
    }
}