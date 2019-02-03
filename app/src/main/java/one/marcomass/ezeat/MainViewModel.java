package one.marcomass.ezeat;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.models.RestaurantMenu;


public class MainViewModel extends ViewModel {
    private MutableLiveData<Integer> currentPage;
    private MutableLiveData<RestaurantMenu> restaurantMenu;
    private MutableLiveData<List<Restaurant>> restaurantList;

    public LiveData<Integer> getCurrentPage() {
        if (currentPage == null) {
            currentPage = new MutableLiveData<>();
            currentPage.setValue(0);
        }
        return currentPage;
    }

    public LiveData<RestaurantMenu> getRestaurantMenu() {
        if (restaurantMenu == null) {
            restaurantMenu = new MutableLiveData<>();
            //TODO chiamata API
        }
        return restaurantMenu;
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        if (restaurantList == null) {
            restaurantList = new MutableLiveData<>();
            //TODO chiamata API
            restaurantList.setValue(getData());
        }
        return restaurantList;
    }

    public void setSelectRestaurant(Restaurant restaurant) {
        currentPage.setValue(1);

    }


    public ArrayList<Restaurant> getData() {
        ArrayList<Restaurant> dataSource = new ArrayList<>();
        dataSource.add(new Restaurant("Primo", "Descrizione del primo ristorante"));
        dataSource.add(new Restaurant("Secondo", "Descrizione del secondo ristorante"));
        dataSource.add(new Restaurant("Terzo", "Descrizione del terzo ristorante"));
        dataSource.add(new Restaurant("Quarto", "Descrizione del quarto ristorante"));
        dataSource.add(new Restaurant("Quinto", "Descrizione del quinto ristorante"));
        dataSource.add(new Restaurant("Sesto", "Descrizione del sesto ristorante"));
        dataSource.add(new Restaurant("Settimo", "Descrizione del settimo ristorante"));
        dataSource.add(new Restaurant("Ottavo", "Descrizione del ottavo ristorante"));
        dataSource.add(new Restaurant("Nono", "Descrizione del nono ristorante"));
        return dataSource;
    }
}
