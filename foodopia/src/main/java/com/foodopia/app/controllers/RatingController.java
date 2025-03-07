package com.foodopia.app.controllers;

import com.foodopia.app.models.users.Rating;
import com.foodopia.app.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/ratings")
public class RatingController {

    private final RatingService ratingService;

    @Autowired
    public RatingController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @PostMapping("/meals/{mealId}")
    public ResponseEntity<Rating> rateMeal(
            @PathVariable String mealId,
            @RequestBody MealRatingRequest ratingRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        String customerId = userDetails.getUsername(); // In a real app, this would be the actual customer ID

        try {
            Rating rating = ratingService.rateMeal(
                    customerId,
                    mealId,
                    ratingRequest.getMealRating(),
                    ratingRequest.getDishRatings(),
                    ratingRequest.getFeedback());

            return ResponseEntity.ok(rating);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{ratingId}")
    public ResponseEntity<Rating> updateRating(
            @PathVariable String ratingId,
            @RequestBody MealRatingRequest ratingRequest,
            @AuthenticationPrincipal UserDetails userDetails) {

        String customerId = userDetails.getUsername(); // In a real app, this would be the actual customer ID

        try {
            Rating rating = ratingService.updateRating(
                    customerId,
                    ratingId,
                    ratingRequest.getMealRating(),
                    ratingRequest.getDishRatings(),
                    ratingRequest.getFeedback());

            return ResponseEntity.ok(rating);
        } catch (IllegalArgumentException | IllegalStateException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/dishes/{dishId}")
    public ResponseEntity<DishRatingResponse> getDishRating(@PathVariable String dishId) {
        Double averageRating = ratingService.getAverageDishRating(dishId);
        Long ratingCount = ratingService.getDishRatingCount(dishId);

        if (averageRating == null || ratingCount == 0) {
            return ResponseEntity.noContent().build();
        }

        DishRatingResponse response = new DishRatingResponse();
        response.setAverageRating(averageRating);
        response.setRatingCount(ratingCount);

        return ResponseEntity.ok(response);
    }

    // Request class for meal ratings
    public static class MealRatingRequest {
        private Double mealRating;
        private Map<String, Double> dishRatings;
        private String feedback;

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

        public String getFeedback() {
            return feedback;
        }

        public void setFeedback(String feedback) {
            this.feedback = feedback;
        }
    }

    // Response class for dish rating
    public static class DishRatingResponse {
        private Double averageRating;
        private Long ratingCount;

        public Double getAverageRating() {
            return averageRating;
        }

        public void setAverageRating(Double averageRating) {
            this.averageRating = averageRating;
        }

        public Long getRatingCount() {
            return ratingCount;
        }

        public void setRatingCount(Long ratingCount) {
            this.ratingCount = ratingCount;
        }
    }
}