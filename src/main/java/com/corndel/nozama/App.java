package com.corndel.nozama;

import com.corndel.nozama.controllers.UserController;
import com.corndel.nozama.controllers.ProductController;
import com.corndel.nozama.controllers.ReviewController;
import com.corndel.nozama.repositories.UserRepository;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;

public class App {

  private Javalin app;

  public static void main(String[] args) {
    var app = new App().javalinApp();
    app.start(8080);
  }

  public App() {
    app = Javalin.create();

    app.post("/users/login", UserController::userLogIn);
    app.get(
        "/",
        ctx -> {
          var users = UserRepository.findAll();
          ctx.json(users);
        });

        app.get("/users/{userId}", ctx -> {
            var id = Integer.parseInt(ctx.pathParam("userId"));
            var user = UserRepository.findById(id);
            ctx.status(HttpStatus.IM_A_TEAPOT).json(user);
        });

// User Routes
      app.post("/users", UserController::createUser);
      app.delete("/users/{userId}", UserController::deleteUser);

// Product Routes
      app.get("/products", ProductController::getAllProducts);
      app.post("/products", ProductController::createProduct);
      app.get("/products/{productId}", ProductController::getProductById);
      app.get("/products/category/{categoryId}", ProductController::getProductsByCategory);

// Product Detail and Review Routes
      app.get("/products/{productId}/reviews", ReviewController::getReviewsByProduct);
      app.post("/products/{productId}/reviews", ReviewController::postReview);
      app.get("/products/{productId}/reviews/average", ReviewController::getAverageRating);


  }

    public Javalin javalinApp() {
        return app;
    }
}
