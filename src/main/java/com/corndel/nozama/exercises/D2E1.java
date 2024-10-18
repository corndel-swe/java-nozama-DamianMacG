package com.corndel.nozama.exercises;

import io.javalin.Javalin;

public class D2E1 {
  /**
   * Creates a Javalin app with a single GET /ping endpoint.
   * This endpoint responds with the simple string "pong".
   *
   * @return a configured Javalin instance
   * @see <a href="https://tech-docs.corndel.com/javalin/creating-a-server.html">...</a>
   * @see <a href="https://tech-docs.corndel.com/javalin/request-response.html">...</a>
   */


  public static Javalin createApp() {
    Javalin app = Javalin.create();

    app.get("/ping", context -> {
      context.result("pong");
    });

    return app;
  }
}