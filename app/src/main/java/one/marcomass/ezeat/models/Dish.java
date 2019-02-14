package one.marcomass.ezeat.models;

import androidx.lifecycle.LiveData;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class Dish {

    private String name;
    private float price;
    private String category;
    private int rating;
    private int ID;
    private int quantity;
    private LiveData<Integer> liveQuantity;

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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getID() {
        return ID;
    }

    public void setLiveQuantity(MainViewModel viewModel) {
        if (liveQuantity == null) {
            liveQuantity = viewModel.getDishQuantity(getID());
        }
    }

    public LiveData<Integer> getLiveQuantity() {
        return liveQuantity;
    }
}
