package com.corndel.nozama.controllers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import com.corndel.nozama.models.Product;
import com.corndel.nozama.repositories.ProductRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;


public class ProductController {

    public static void getAllProducts(Context ctx) throws SQLException {
        List<Product> products = ProductRepository.findAll();
        ctx.json(products);
    }

    public static void getProductById(Context ctx) throws SQLException {
        // Get the product ID from the path parameters
        String idParam = ctx.pathParam("id");

        // Parse the string ID to an integer
        int id = Integer.parseInt(idParam);

        Product product = ProductRepository.findById(id);

        if (product != null) {
            ctx.json(product);
        } else {
            ctx.status(404).result("Product not found");
        }
    }


    public static void createProduct(Context ctx) throws SQLException {
        Product product = ctx.bodyAsClass(Product.class);
        ProductRepository.createProduct(product);
        ctx.status(201).json(Map.of("product", product));
    }



    public static void getProductsByCategory(Context ctx) throws SQLException {
        String categoryIdParam = ctx.pathParam("categoryId");
            int categoryId = Integer.parseInt(categoryIdParam);
            List<Product> products = ProductRepository.findByCategory(categoryId);
            ctx.json(products);
    }


}
