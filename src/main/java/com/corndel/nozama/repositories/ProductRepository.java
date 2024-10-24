package com.corndel.nozama.repositories;

import com.corndel.nozama.DB;
import com.corndel.nozama.models.Product;
import io.javalin.http.NotFoundResponse;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    // Retrieve all products
    public static List<Product> findAll() throws SQLException {
        var query = "SELECT id, name, description, price, stockQuantity, imageURL FROM products";

        try (var con = DB.getConnection();
             var stmt = con.createStatement();
             var rs = stmt.executeQuery(query)) {

            var products = new ArrayList<Product>();
            while (rs.next()) {
                var id = rs.getInt("id");
                var name = rs.getString("name");
                var description = rs.getString("description");
                var price = rs.getFloat("price");
                var stockQuantity = rs.getInt("stockQuantity");
                var imageURL = rs.getString("imageURL");

                products.add(new Product(id, name, description, price, stockQuantity, imageURL));
            }

            return products;
        }
    }

    // Retrieve a product by ID
    public static Product findById(int id) throws SQLException {
        var query = "SELECT id, name, description, price, stockQuantity, imageURL FROM products WHERE id = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {

            stmt.setInt(1, id);
            try (var rs = stmt.executeQuery()) {
                if (rs.next()) {
                    var name = rs.getString("name");
                    var description = rs.getString("description");
                    var price = rs.getFloat("price");
                    var stockQuantity = rs.getInt("stockQuantity");
                    var imageURL = rs.getString("imageURL");

                    return new Product(id, name, description, price, stockQuantity, imageURL);
                } else {
                    return null;
                }
            }
        }
    }

    // Create a new product
    public static void createProduct(Product product) throws SQLException {
        var query = "INSERT INTO products (name, description, price, stockQuantity, imageURL) VALUES (?, ?, ?, ?, ?)";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            // Removed ID from the insert statement, assuming it's auto-increment
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setFloat(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getImageURL());

            stmt.executeUpdate();
        }
    }

    // Update a product by ID
    public static void updateProduct(int id, Product product) throws SQLException {
        var query = "UPDATE products SET name = ?, description = ?, price = ?, stockQuantity = ?, imageURL = ? WHERE id = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setFloat(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getImageURL());
            stmt.setInt(6, id); // Change to setInt

            stmt.executeUpdate();
        }
    }

    // Delete a product by ID
    public static void deleteProduct(int id) throws SQLException {
        var query = "DELETE FROM products WHERE id = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setInt(1, id); // Change to setInt
            stmt.executeUpdate();
        }
    }

    // Find products by category ID
    public static List<Product> findByCategory(int categoryId) throws SQLException {
        var query = "SELECT p.id, p.name, p.description, p.price, p.stockQuantity, p.imageURL FROM products p " +
                "JOIN product_categories pc ON p.id = pc.productId " +
                "WHERE pc.categoryId = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setInt(1, categoryId);
            try (var rs = stmt.executeQuery()) {
                var products = new ArrayList<Product>();
                while (rs.next()) {
                    var id = rs.getInt("id");
                    var name = rs.getString("name");
                    var description = rs.getString("description");
                    var price = rs.getFloat("price");
                    var stockQuantity = rs.getInt("stockQuantity");
                    var imageURL = rs.getString("imageURL");

                    products.add(new Product(id, name, description, price, stockQuantity, imageURL)); // Update constructor
                }

                return products;
            }
        }
    }

    public static void updateProductPartial(int id, Product product) throws SQLException {

        //COALESCE allows us to keep the values already there if not updated with the request - helping avoid null errors
        var query = "UPDATE products SET " +
                "name = COALESCE(?, name), " +
                "description = COALESCE(?, description), " +
                "price = COALESCE(?, price), " +
                "stockQuantity = COALESCE(?, stockQuantity), " +
                "imageURL = COALESCE(?, imageURL) " +
                "WHERE id = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setFloat(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getImageURL());
            stmt.setInt(6, id);

            stmt.executeUpdate();
        }
    }


}

