package ssf.day19.models;

import java.io.StringReader;
import java.util.Date;

import jakarta.json.Json;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

public class Product {
    private int id;
    private String title;
    private String description;
    private float price;
    private float discountPercentage;
    private float rating;
    private int stock; 
    private String brand;
    private String category;
    private Date dated;
    private int buy;                // to keep track how many of this product has been bought
    
    public Product() {}
    public Product(int id, String title, String description, float price, float discountPercentage, float rating,
            int stock, String brand, String category, Date dated, int buy) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.discountPercentage = discountPercentage;
        this.rating = rating;
        this.stock = stock;
        this.brand = brand;
        this.category = category;
        this.dated = dated;
        this.buy = buy;
    }

    public static Product jsonToProduct(String json) {
        if (json == null)
            return null;

        JsonReader reader = Json.createReader(new StringReader(json));
        JsonObject j = reader.readObject();

        // Convert String epoch ms -> Long -> Date
        Date dated = new Date(j.getJsonNumber("dated").longValue());

        Product product = new Product(j.getInt("id"),
                        j.getString("title"),
                        j.getString("description"),
                        (float) j.getJsonNumber("price").doubleValue(),
                        (float) j.getJsonNumber("discountPercentage").doubleValue(),
                        (float) j.getJsonNumber("rating").doubleValue(),
                        j.getInt("stock"),
                        j.getString("brand"),
                        j.getString("category"),
                        dated,
                        j.getInt("buy"));
                        
        return product;
    }

    public String toJson() {
        JsonObject job = Json.createObjectBuilder()
                        .add("id", this.id)
                        .add("title", this.title)
                        .add("description", this.description)
                        .add("price", this.price)
                        .add("discountPercentage", this.discountPercentage)
                        .add("rating", this.rating)
                        .add("stock", this.stock)
                        .add("brand", this.brand)
                        .add("category", this.category)
                        .add("dated", this.dated.getTime())
                        .add("buy", this.buy)
                        .build();

        return job.toString();
    }

    @Override
    public String toString() {
        return "Product [id=" + id + ", title=" + title + ", description=" + description + ", price=" + price
                + ", discountPercentage=" + discountPercentage + ", rating=" + rating + ", stock=" + stock + ", brand="
                + brand + ", category=" + category + ", buy=" + buy + "]";
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
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
    public float getDiscountPercentage() {
        return discountPercentage;
    }
    public void setDiscountPercentage(float discountPercentage) {
        this.discountPercentage = discountPercentage;
    }
    public float getRating() {
        return rating;
    }
    public void setRating(float rating) {
        this.rating = rating;
    }
    public int getStock() {
        return stock;
    }
    public void setStock(int stock) {
        this.stock = stock;
    }
    public String getBrand() {
        return brand;
    }
    public void setBrand(String brand) {
        this.brand = brand;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public Date getDated() {
        return dated;
    }
    public void setDated(Date dated) {
        this.dated = dated;
    }
    public int getBuy() {
        return buy;
    }
    public void setBuy(int buy) {
        this.buy = buy;
    }

    
}
