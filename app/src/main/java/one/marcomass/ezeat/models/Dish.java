package one.marcomass.ezeat.models;

import androidx.lifecycle.LiveData;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.viewmodels.OrderViewModel;

public class Dish {

    private String name;
    private String image_url;
    private float price;
    private String id;
    private int quantity;
    private String restaurant;
    private LiveData<Integer> liveQuantity;

    public Dish(String name, String image_url, float price, String id, String restaurant) {
        this.name = name;
        this.image_url = image_url;
        this.price = price;
        this.id = id;
        setQuantity(0);
    }

    public String getName() {
        return name;
    }

    public float getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getID() {
        return id;
    }

    public String getImageUrl() {
        return image_url;
    }

    public String getRestaurant() {
        return restaurant;
    }

    public LiveData<Integer> setLiveQuantity(OrderViewModel viewModel) {
        if (liveQuantity == null) {
            liveQuantity = viewModel.getDishQuantity(getID());
        }
        return liveQuantity;
    }

    public LiveData<Integer> getLiveQuantity() {
        return liveQuantity;
    }
}
