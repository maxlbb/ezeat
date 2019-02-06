package one.marcomass.ezeat;

import android.app.Application;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Cart;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.models.RestaurantMenu;
import one.marcomass.ezeat.repos.CartRepository;


public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> currentPage;
    private MutableLiveData<ArrayList<Object>> restaurantMenu;
    private MutableLiveData<ArrayList<Restaurant>> restaurantList;
    private Cart cart;

    private CartRepository cartRepository;
    private LiveData<List<DishEntity>> allDishes;
    private LiveData<Integer> cartDishCount;

    public MainViewModel(Application application) {
        super(application);
        cart = new Cart();
        cartRepository = new CartRepository(application);
        allDishes = cartRepository.getAllDishes();
        cartDishCount = Transformations.map(allDishes, new Function<List<DishEntity>, Integer>() {
            @Override
            public Integer apply(List<DishEntity> input) {
                return input.size();
            }
        });

        currentPage = new MutableLiveData<>();
        currentPage.setValue(0);
    }

    //Cart management ------------------------------------------------------------------------------

    public LiveData<List<DishEntity>> getCartAllDishes() {
        return allDishes;
    }

    public void addDishToCart(Dish dish) {
        cartRepository.insert(new DishEntity(0, dish.getID()));
    }

    public void removeAllFromCart() {
        cartRepository.removeAll();
    }

    public void removeDishFromCart(Dish dish) {
        cartRepository.removeDish(dish.getID());
    }

    public LiveData<Integer> getCartDishCount() {
        return cartDishCount;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Integer> getCurrentPage() {
        return currentPage;
    }
    public LiveData<ArrayList<Object>> getRestaurantMenu() {
        if (restaurantMenu == null) {
            restaurantMenu = new MutableLiveData<>();
            //TODO chiamata API
            restaurantMenu.setValue(getMenuMock());
        }
        return restaurantMenu;
    }

    public LiveData<ArrayList<Restaurant>> getRestaurantList() {
        if (restaurantList == null) {
            restaurantList = new MutableLiveData<>();
            //TODO chiamata API
            restaurantList.setValue(getRestaurantMock());
        }
        return restaurantList;
    }

    public void setSelectRestaurant(Restaurant restaurant) {
        currentPage.setValue(1);
    }


    public ArrayList<Restaurant> getRestaurantMock() {
        ArrayList<Restaurant> dataSource = new ArrayList<>();
        dataSource.add(new Restaurant(0, "Primo", "Descrizione del primo ristorante"));
        dataSource.add(new Restaurant(1, "Secondo", "Descrizione del secondo ristorante"));
        dataSource.add(new Restaurant(2, "Terzo", "Descrizione del terzo ristorante"));
        dataSource.add(new Restaurant(3, "Quarto", "Descrizione del quarto ristorante"));
        dataSource.add(new Restaurant(4, "Quinto", "Descrizione del quinto ristorante"));
        dataSource.add(new Restaurant(5, "Sesto", "Descrizione del sesto ristorante"));
        dataSource.add(new Restaurant(6, "Settimo", "Descrizione del settimo ristorante"));
        dataSource.add(new Restaurant(7, "Ottavo", "Descrizione del ottavo ristorante"));
        dataSource.add(new Restaurant(8, "Nono", "Descrizione del nono ristorante"));
        return dataSource;
    }

    public ArrayList<Object> getMenuMock() {
        ArrayList<Object> dataSource = new ArrayList<>();
        dataSource.add("Primi");
        dataSource.add(new Dish("Pasta", 4.99f, "Primo", 3, 0));
        dataSource.add(new Dish("Lasagne", 4.99f, "Primo", 4, 1));
        dataSource.add(new Dish("Gnocchi", 4.99f, "Primo", 5, 2));
        dataSource.add("Secondi");
        dataSource.add(new Dish("Coscia di pollo", 4.99f, "Secondo", 4, 3));
        dataSource.add(new Dish("Uovo sbattuto", 4.99f, "Secondo", 4, 4));
        dataSource.add(new Dish("Vitello tonnato", 4.99f, "Secondo", 5, 5));
        dataSource.add("Bevande");
        dataSource.add(new Dish("Acqua", 4.99f, "Bevanda", 2, 6));
        dataSource.add(new Dish("Cocacola", 4.99f, "Bevanda", 5, 7));
        dataSource.add(new Dish("Fanta", 4.99f, "Bevanda", 5, 8));
        dataSource.add(new Dish("Birra", 4.99f, "Bevanda", 2, 9));
        dataSource.add(new Dish("Vino", 4.99f, "Bevanda", 5, 10));
        return dataSource;
    }
}
