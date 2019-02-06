package one.marcomass.ezeat.models;

public class Dish {

    private String name;
    private float price;
    private String category;
    private int rating;
    private int ID;
    private int quantity;

    public Dish(String name, float price, String category, int rating, int ID) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.rating = rating;
        this.quantity = 0;
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public String getCategory() {
        return category;
    }

    public int getRating() { return rating; }

    public int getQuantity() {
        return quantity;
    }

    public int getID() {
        return ID;
    }
}
