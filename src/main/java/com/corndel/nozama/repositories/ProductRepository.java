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
                var id = rs.getString("id");
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

    public static Product findById(String id) throws SQLException {
        var query = "SELECT id, name, description, price, stockQuantity, imageURL FROM products WHERE id = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {

            stmt.setString(1, id);
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

    public static void createProduct(Product product) throws SQLException {
        var query = "INSERT INTO products (id, name, description, price, stockQuantity, imageURL) VALUES (?, ?, ?, ?, ?, ?)";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setString(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getDescription());
            stmt.setFloat(4, product.getPrice());
            stmt.setInt(5, product.getStockQuantity());
            stmt.setString(6, product.getImageURL());

            stmt.executeUpdate();
        }
    }

    public static List<Product> findByCategory(String categoryId) throws SQLException {
        var query = "SELECT p.id, p.name, p.description, p.price, p.stockQuantity, p.imageURL FROM products p " +
                "JOIN product_categories pc ON p.id = pc.productId " +
                "WHERE pc.categoryId = ?";

        try (var con = DB.getConnection();
             var stmt = con.prepareStatement(query)) {
            stmt.setString(1, categoryId);
            try (var rs = stmt.executeQuery()) {
                var products = new ArrayList<Product>();
                while (rs.next()) {
                    var id = rs.getString("id");
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


    public static void main(String[] args) {
        try {
            // Test findAll
//            System.out.println("All Products:");
//            List<Product> products = findAll();
//            for (Product product : products) {
//                System.out.println(product);
//            }

            // Test findById
            String productId = "300";
            Product product = findById(productId);
            if (product != null) {
                System.out.println("Product found: " + product);
            } else {
                System.out.println("Product not found.");
            }

            // Test createProduct
//            Product newProduct = new Product("300", "New Product", "This is a new product", 19.99f, 100, "http://example.com/image.jpg");
//            createProduct(newProduct);
//            System.out.println("Product created: " + newProduct);

            // Test findByCategory
//            String categoryId = "7";
//            System.out.println("Products in Category " + categoryId + ":");
//            List<Product> categoryProducts = findByCategory(categoryId);
//            for (Product catProduct : categoryProducts) {
//                System.out.println(catProduct);
//            }

        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

}

