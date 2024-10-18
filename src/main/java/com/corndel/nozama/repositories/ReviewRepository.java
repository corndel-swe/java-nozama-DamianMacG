package com.corndel.nozama.repositories;

import com.corndel.nozama.DB;
import com.corndel.nozama.models.Product;
import com.corndel.nozama.models.Review;
import io.javalin.http.BadRequestResponse;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ReviewRepository {
  public static List<Review> getReviewsByProduct(int productId) throws SQLException {
    List<Review> reviews = new ArrayList<>();
    var query = "SELECT * FROM reviews JOIN products ON products.id = reviews.productId WHERE reviews.productId = ?";

    try (var connection = DB.getConnection();
         var stmt = connection.prepareStatement(query)) {

      stmt.setInt(1, productId);

      try (ResultSet resultSet = stmt.executeQuery()) {
        while (resultSet.next()) {
          int id = resultSet.getInt("id");
          int userId = resultSet.getInt("userId");
          int rating = resultSet.getInt("rating");
          String reviewText = resultSet.getString("reviewText");
          String reviewDate = resultSet.getString("reviewDate");
          Review review = new Review(productId, id, userId, rating, reviewText, reviewDate);
          reviews.add(review);
        }
      }
    }
    return reviews;
  }

  public static Review postReview(int productId, int userId, int rating, String reviewText) throws SQLException {


    var query = "INSERT INTO reviews (productId, userId, rating, reviewText) VALUES (?, ?, ?, ?) RETURNING id, reviewDate;";

    int lastId = -1;
    String reviewDate = null;

    try (var con = DB.getConnection();
         var stmt = con.prepareStatement(query)) {
      stmt.setInt(1, productId);
      stmt.setInt(2, userId);
      stmt.setInt(3, rating);
      stmt.setString(4, reviewText);


      try (var rs = stmt.executeQuery()) {
        if (rs.next()) { // Only one row should be returned due to the insert
          lastId = rs.getInt("id");
          reviewDate = rs.getString("reviewDate");
        }
      }
    } catch (SQLException e) {
      // Handle SQL exceptions appropriately
      throw new SQLException("Error inserting review: " + e.getMessage(), e);
    }

    // Return a new Review object with the retrieved values
    return new Review(lastId, productId,  userId, rating, reviewText, reviewDate);
  }

  public static float getAverageRating(int productId) throws SQLException {

    var query = "SELECT AVG(reviews.rating) as averageRating FROM reviews JOIN products ON products.id = reviews.productId WHERE reviews.productId = ?";

      float averageRating = 0;
      try (var connection = DB.getConnection();
           var stmt = connection.prepareStatement(query)) {

          stmt.setInt(1, productId);

          try (ResultSet resultSet = stmt.executeQuery()) {
              while (resultSet.next()) {
                averageRating =  resultSet.getFloat("averageRating");
              }
          }
      }
      if (averageRating <= 0) {
        throw new BadRequestResponse("Product not found");
      }
      return averageRating;
  }


}
