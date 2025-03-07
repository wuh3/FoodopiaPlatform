package com.foodopia.app.repository;

import com.foodopia.app.models.users.Rating;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface RatingRepository extends MongoRepository<Rating, String> {

    // Find rating by customer and meal
    Optional<Rating> findByCustomerIdAndMealId(String customerId, String mealId);

    // Find all ratings for a meal
    List<Rating> findByMealId(String mealId);

    // Find all ratings from a customer
    List<Rating> findByCustomerId(String customerId);

    // Find ratings within a time period
    List<Rating> findByCreatedAtBetween(Date startDate, Date endDate);

    // Find ratings for a specific dish
    @Query("{'dishRatings.?0': {$exists: true}}")
    List<Rating> findByDishRated(String dishId);

    // Find average rating for a dish using aggregation
    @Aggregation(pipeline = {
            "{ $match: { 'dishRatings.?0': { $exists: true } } }",
            "{ $group: { _id: null, avgRating: { $avg: '$dishRatings.?0' } } }"
    })
    Double findAverageRatingForDish(String dishId);

    // Find average meal rating
    @Aggregation(pipeline = {
            "{ $group: { _id: null, avgRating: { $avg: '$mealRating' } } }"
    })
    Double findAverageMealRating();

    // Find dish ratings in a time period
    @Query("{'dishRatings.?0': {$exists: true}, 'createdAt': {$gte: ?1, $lte: ?2}}")
    List<Rating> findDishRatingsInPeriod(String dishId, Date startDate, Date endDate);

    // Count the number of ratings for a dish
    @Query(value = "{'dishRatings.?0': {$exists: true}}", count = true)
    Long countRatingsForDish(String dishId);
}