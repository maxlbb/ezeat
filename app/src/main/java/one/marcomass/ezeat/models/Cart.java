package one.marcomass.ezeat.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class Cart {

    private ArrayList<Dish> dishes;
    private float total = 0;
    private MutableLiveData<Integer> dishCount;
    private MutableLiveData<ArrayList<Dish>> livDishes;

    public Cart() {
        dishes = new ArrayList<>();
    }

    public void addDish(Dish dish) {
        dishes.add(dish);
        //TODO maybe init dishCount in constructor
        if (dishCount != null) {
            dishCount.setValue(dishes.size());
        }
    }

    public ArrayList<Dish> getDishes() {
        return dishes;
    }

    public LiveData<Integer> getDishCount() {
        if (dishCount == null) {
            dishCount = new MutableLiveData<>();
            dishCount.setValue(dishes.size());
        }
        return dishCount;
    }
}
