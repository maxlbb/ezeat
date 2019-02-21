package one.marcomass.ezeat.models;

import java.util.List;

public class Menu {

    private final List<Dish> products;
    private final String name;
    private final String description;
    private final String image_url;
    private final String address;
    private final String phone_number;
    private final float min_order;
    private final int rating;
    private final String id;

    public Menu(List<Dish> products, String name, String description, String image_url, String address, String phone_number, float min_order, int rating, String id) {
        this.products = products;
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.address = address;
        this.phone_number = phone_number;
        this.min_order = min_order;
        this.rating = rating;
        this.id = id;
    }

    public List<Dish> getProducts() {
        return products;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImage_url() {
        return image_url;
    }

    public String getAddress() {
        return address;
    }

    public String getPhoneNumber() {
        return phone_number;
    }

    public float getMinOrder() {
        return min_order;
    }

    public int getRating() {
        return rating;
    }

    public String getId() {
        return id;
    }
}
