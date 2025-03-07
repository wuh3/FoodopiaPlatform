package com.foodopia.app.controllers;

import com.foodopia.app.service.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dishes/popular")
public class PopularDishController {

    private final RatingService ratingService;

    @Autowired
    public PopularDishController(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getPopularDishes(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ratingService.getPopularDishes(limit));
    }

    @GetMapping("/previous-month")
    public ResponseEntity<List<Map<String, Object>>> getPreviousMonthPopularDishes(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(ratingService.getPreviousMonthPopularDishes(limit));
    }
}