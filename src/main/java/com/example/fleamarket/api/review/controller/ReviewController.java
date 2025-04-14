package com.example.fleamarket.api.review.controller;

import com.example.fleamarket.api.review.dto.RatedUserDto;
import com.example.fleamarket.api.review.entity.Review;
import com.example.fleamarket.api.review.exception.AlreadyReviewedException;
import com.example.fleamarket.api.review.input.ReviewInput;
import com.example.fleamarket.api.review.service.ReviewService;
import com.example.fleamarket.api.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/review")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping("/reviews")
    public ResponseEntity<Void> review(@RequestBody ReviewInput reviewInput, @AuthenticationPrincipal User user) {

        Review review = null;
        if (reviewInput.getAsBuyer()) {
            review = this.reviewService.reviewAsBuyer(reviewInput, user);
        } else {
            review = this.reviewService.reviewAsSeller(reviewInput, user);
        }
        URI location = ServletUriComponentsBuilder.fromCurrentRequestUri()
            .path("/{id}")
            .buildAndExpand(review.getId())
            .toUri();
        return ResponseEntity.created(location).build();
    }


    @GetMapping("/rated-users/{userId}")
    public RatedUserDto getRatedUser(@PathVariable String userId) {
        return this.reviewService.getRatedUser(userId);
    }


    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, Object> handleAlreadyReviewdException(AlreadyReviewedException ex) {
        Map<String, Object> map = new HashMap<>();
        map.put("cause", "AlreadyReviewed");
        return map;
    }

}
