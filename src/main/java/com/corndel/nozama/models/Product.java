package com.corndel.nozama.models;

public class Product {
    private int id; // Changed from String to int
    private String name;
    private String description;
    private float price;
    private int stockQuantity;
    private String imageURL;

    public Product() {
    }

    public Product(int id, String name, String description, float price, int stockQuantity, String imageURL) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageURL = imageURL;
    }

    public Product(String name, String description, float price, int stockQuantity, String imageURL) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageURL = imageURL;
    }

    public int getId() { // Return type is now int
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", price=" + price +
                ", stockQuantity=" + stockQuantity +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
