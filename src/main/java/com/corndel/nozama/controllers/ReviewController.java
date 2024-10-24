package com.corndel.nozama.controllers;
import com.corndel.nozama.models.Review;
import com.corndel.nozama.repositories.ReviewRepository;
import io.javalin.http.BadRequestResponse;
import io.javalin.http.Context;
import io.javalin.http.ForbiddenResponse;
import io.javalin.http.HttpStatus;

import java.sql.SQLException;
import java.util.Map;


public class ReviewController {
    record ReviewRequest(Integer productId, Integer userId, Integer rating, String reviewText) {}
    public static void getReviewsByProduct(Context ctx)  {
        try {
            int id = Integer.parseInt(ctx.pathParam("productId"));
            var reviews = ReviewRepository.getReviewsByProduct(id);
            ctx.status(200).json(reviews);
        } catch (Exception e) {
            ctx.status(404);
            throw new BadRequestResponse("Product not found.");
        }
    }

    public static void postReview(Context ctx) {
        String requestBody = ctx.body(); // Get raw request body
        System.out.println("Request body: " + requestBody);

        try {
            ReviewRequest newReview = ctx.bodyAsClass(ReviewRequest.class);
            Review createdReview = ReviewRepository.postReview(newReview.productId(), newReview.userId(), newReview.rating(), newReview.reviewText());
            ctx.status(201).json(createdReview);
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("Invalid request body: " + e.getMessage());
            e.printStackTrace();      }
    }

    public static void getAverageRating(Context ctx) {

        try {
            int id = Integer.parseInt(ctx.pathParam("productId"));
            var averageRating = ReviewRepository.getAverageRating(id);
            ctx.status(200).json(Map.of("averageRating", averageRating));
        } catch (Exception e) {
            ctx.status(404);
            throw new BadRequestResponse("Product Not Found.");
        }
    }


}
