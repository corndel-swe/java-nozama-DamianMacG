package com.corndel.nozama.controllers;

import io.javalin.http.Context;
import io.javalin.http.NotFoundResponse;
import com.corndel.nozama.models.Product;
import com.corndel.nozama.repositories.ProductRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductController {

    // GET all products
    public static void getAllProducts(Context ctx) {
        try {
            List<Product> products = ProductRepository.findAll();
            ctx.status(200).json(products);
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }

    // GET product by ID
    public static void getProductById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Product product = ProductRepository.findById(id);
            if (product != null) {
                ctx.status(200).json(product);
            } else {
                ctx.status(404).json("Product not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid product ID format.");
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }

    // POST (create) a new product
    public static void createProduct(Context ctx) {
        try {
            Product product = ctx.bodyAsClass(Product.class);
            ProductRepository.createProduct(product);
            ctx.status(201).json(product);
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }

    // PUT (update) a product by ID
    public static void updateProduct(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Product updatedProduct = ctx.bodyAsClass(Product.class);
            Product existingProduct = ProductRepository.findById(id);

            if (existingProduct != null) {
                ProductRepository.updateProduct(id, updatedProduct);
                ctx.status(200).json(updatedProduct);
            } else {
                ctx.status(404).json("Product not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid product ID format.");
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }

    // DELETE a product by ID
    public static void deleteProduct(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("id"));
            Product product = ProductRepository.findById(id);
            if (product != null) {
                ProductRepository.deleteProduct(id);
                ctx.status(204);
            } else {
                ctx.status(404).json("Product not found");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid product ID format.");
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }


    // GET products by category ID
    public static void getProductsByCategory(Context ctx) {
        try {
            int categoryId = Integer.parseInt(ctx.pathParam("categoryId"));
            List<Product> products = ProductRepository.findByCategory(categoryId);
            if (!products.isEmpty()) {
                ctx.status(200).json(products);
            } else {
                ctx.status(404).json("No products found for this category");
            }
        } catch (NumberFormatException e) {
            ctx.status(400).json("Invalid category ID format.");
        } catch (SQLException e) {
            ctx.status(500).json("Database error: " + e.getMessage());
        }
    }

    // PATCH (partially update) a product by ID
    public static void patchProduct(Context ctx) {
        int id = Integer.parseInt(ctx.pathParam("id"));
        try {
            Product existingProduct = ProductRepository.findById(id);
            if (existingProduct != null) {
                Product updatedFields = ctx.bodyAsClass(Product.class);

                // Ensure ID remains unchanged and not included in the request
                if (updatedFields.getId() != 0) {
                    ctx.status(400).result("ID cannot be updated.");
                    return;
                }

                // Only update fields that are present in the request body
                if (updatedFields.getName() != null) {
                    existingProduct.setName(updatedFields.getName());
                }
                if (updatedFields.getDescription() != null) {
                    existingProduct.setDescription(updatedFields.getDescription());
                }
                if (updatedFields.getPrice() != 0) {
                    existingProduct.setPrice(updatedFields.getPrice());
                }
                if (updatedFields.getStockQuantity() >= 0) {
                    existingProduct.setStockQuantity(updatedFields.getStockQuantity());
                }
                if (updatedFields.getImageURL() != null) {
                    existingProduct.setImageURL(updatedFields.getImageURL());
                }

                ProductRepository.updateProductPartial(id, existingProduct);
                ctx.status(200).json(existingProduct);
            } else {
                throw new NotFoundResponse("Product not found");
            }
        } catch (SQLException e) {
            ctx.status(500).result("Database error: " + e.getMessage());
        } catch (NumberFormatException e) {
            ctx.status(400).result("Invalid product ID format.");
        }
    }

}
