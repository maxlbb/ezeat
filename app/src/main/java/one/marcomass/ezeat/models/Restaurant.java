package one.marcomass.ezeat.models;

public class Restaurant {

    private final String name;
    private final String description;
    private final String image_url;
    private final String address;
    private final String phone_number;
    private final float min_order;
    private final int rating;
    private final String id;

    public Restaurant(String name, String description, String image_url, String address, String phone_number, float min_order, int rating, String id) {
        this.name = name;
        this.description = description;
        this.image_url = image_url;
        this.address = address;
        this.phone_number = phone_number;
        this.min_order = min_order;
        this.rating = rating;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getImageURL() {
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

    public String getID() {
        return id;
    }
}
