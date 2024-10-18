package com.corndel.nozama.exercises;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class D2E2 {
    /**
     * Creates a Javalin app with two endpoints:
     *
     * - GET /sumup responds with the sum of the integers from 1 to n, where n is
     * provided as a query parameter. If n is not provided, it responds with 0.
     *
     * - GET /multiply/{x}/{y} responds with the product of x and y.
     *
     * @see <a href="https://tech-docs.corndel.com/javalin/query-params.html">...</a>
     * @see <a href="https://tech-docs.corndel.com/javalin/url-params.html">...</a>
     * @return a configured Javalin instance
     */
    public static Javalin createApp() {
        var app = Javalin.create();

        app.get("/sumup", ctx -> {
            String nParam = ctx.queryParam("n");
            int n = (nParam != null) ? Integer.parseInt(nParam) : 0;

            int sum = 0;
            for (int i = 1; i <= n; i++) {
                sum += i;
            }
            ctx.result(String.valueOf(sum));
        });

        app.get("/multiply/{x}/{y}", ctx -> {
            int x = Integer.parseInt(ctx.pathParam("x"));
            int y = Integer.parseInt(ctx.pathParam("y"));
            int product = x * y;
            ctx.result(String.valueOf(product));
        });

        return app;
    }
}

