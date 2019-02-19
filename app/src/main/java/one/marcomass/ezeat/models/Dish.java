package one.marcomass.ezeat.models;

import androidx.lifecycle.LiveData;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class Dish {

    private String name;
    private String image_url;
    private float price;
    private String id;
    private int quantity;
    private LiveData<Integer> liveQuantity;

    public Dish(String name, String image_url, float price, String id) {
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

    public LiveData<Integer> setLiveQuantity(MainViewModel viewModel) {
        if (liveQuantity == null) {
            liveQuantity = viewModel.getDishQuantity(getID());
        }
        return liveQuantity;
    }

    public LiveData<Integer> getLiveQuantity() {
        return liveQuantity;
    }
}
