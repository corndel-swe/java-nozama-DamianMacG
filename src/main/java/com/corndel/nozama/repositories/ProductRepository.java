package com.corndel.nozama.repositories;

import com.corndel.nozama.DB;
import com.corndel.nozama.models.Product;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    public static List<Product> findAll() throws SQLException {
        var query = "SELECT id, name, description, price, stockQuantity, imageURL FROM products";

        try (var con = DB.getConnection();
             var stmt = con.createStatement();
             var rs = stmt.executeQuery(query)) {

            var products = new ArrayList<Product>();
            while (rs.next()) {
                // Use getInt for id since it is INTEGER in the database
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
                    return null; // Product not found
                }
            }
        }
    }

    public static void createProduct(Product product) throws SQLException {
        var query = "INSERT INTO products (name, description, price, stockQuantity, imageURL) VALUES (?, ?, ?, ?, ?)";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query, java.sql.Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, product.getName());
            stmt.setString(2, product.getDescription());
            stmt.setFloat(3, product.getPrice());
            stmt.setInt(4, product.getStockQuantity());
            stmt.setString(5, product.getImageURL());

            int rowsAffected = stmt.executeUpdate();

            try (var generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    product.setId(generatedKeys.getInt(1));
                }
            }
        }
    }


    public static List<Product> findByCategory(int categoryId) throws SQLException {
        var query = "SELECT p.id, p.name, p.description, p.price, p.stockQuantity, p.imageURL " +
                "FROM products p " +
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

                    products.add(new Product(id, name, description, price, stockQuantity, imageURL));
                }
                return products;
            }
        }
    }
}
