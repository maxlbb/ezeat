package one.marcomass.ezeat.viewmodels;

import android.app.Application;
import android.util.Log;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.ArrayList;
import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import one.marcomass.ezeat.RestaurantAPI;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.repos.CartRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> bottomSheetOpen;

    private MutableLiveData<List<Object>> restaurantMenu;
    private MutableLiveData<List<Restaurant>> restaurantList;

    private CartRepository cartRepository;
    private LiveData<List<DishEntity>> allDishes;
    private LiveData<Integer> cartAllDishCount;
    private LiveData<Float> cartTotal;


    public MainViewModel(Application application) {
        super(application);
        cartRepository = new CartRepository(application);
        allDishes = cartRepository.getAllDishes();

        cartAllDishCount = Transformations.map(allDishes, new Function<List<DishEntity>, Integer>() {
            @Override
            public Integer apply(List<DishEntity> input) {
                int totalQuantity = 0;
                for (DishEntity dishEntity : input) {
                    totalQuantity += dishEntity.getQuantity();
                }
                return totalQuantity;
            }
        });

        cartTotal = cartRepository.getTotal();

        bottomSheetOpen = new MutableLiveData<>();
        bottomSheetOpen.setValue(BottomSheetBehavior.STATE_COLLAPSED);
    }

    //Cart management ------------------------------------------------------------------------------

    public LiveData<List<DishEntity>> getCartAllDishes() {
        return cartRepository.getAllDishes();
    }

    public void addDishToCart(Dish dish) {
        cartRepository.add(new DishEntity(0, dish.getID(), 1, dish.getPrice(), dish.getName()));
    }

    public void removeDishFromCart(Dish dish) {
        cartRepository.removeDish(dish.getID());
    }

    public void removeAllDishFromCart(Dish dish) { cartRepository.removeAllDish(dish.getID()); }

    public void removeAllFromCart() {
        cartRepository.removeAll();
    }

    public LiveData<Integer> getCartAllDishCount() {
        return cartAllDishCount;
    }

    public LiveData<Integer> getDishQuantity(int dishID) {
        return cartRepository.getDishQuantity(dishID);
    }

    public LiveData<Float> getCartTotal() {
        return cartTotal;
    }

    //TODO API or something from repo
    public LiveData<Dish> getDishFromID(int dishID) {
        MutableLiveData<Dish> dish = new MutableLiveData<>();
        dish.setValue(findInMenuMock(dishID));
        return dish;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Integer> getBottomSheetState() {
        return bottomSheetOpen;
    }

    public void setBottomSheetOpen(int state) {
        bottomSheetOpen.setValue(state);
    }

    public LiveData<List<Object>> getRestaurantMenu() {
        if (restaurantMenu == null) {
            restaurantMenu = new MutableLiveData<>();
            //TODO chiamata API
            restaurantMenu.setValue(getMenuMock());
        }
        return restaurantMenu;
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        if (restaurantList == null) {
            restaurantList = new MutableLiveData<>();
            loadRestaurants();
        }
        return restaurantList;
    }

    private void loadRestaurants() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestaurantAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        RestaurantAPI restaurantAPI = retrofit.create(RestaurantAPI.class);
        Call<List<Restaurant>> call = restaurantAPI.getRestaurants();

        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                Log.d("Retrofit", "Response " + response.code());
                Log.d("Retrofit", "Response " + response.body().get(0).getName());
                restaurantList.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                Log.e("Retrofit", "Restaurant request fail");
                Log.e("Retrofit", t.getMessage());
                t.printStackTrace();
            }
        });
    }

    /*public ArrayList<Restaurant> getRestaurantMock() {
        ArrayList<Restaurant> dataSource = new ArrayList<>();
        dataSource.add(new Restaurant(0, "Primo", "Indirizzo 1", 8.30f, null));
        dataSource.add(new Restaurant(1, "Secondo", "Indirizzo 2", 8.30f, null));
        dataSource.add(new Restaurant(2, "Terzo", "Indirizzo 3", 4.50f, null));
        dataSource.add(new Restaurant(3, "Quarto", "Indirizzo 4", 1.80f, null));
        dataSource.add(new Restaurant(4, "Quinto", "Indirizzo 5", 8.30f, null));
        dataSource.add(new Restaurant(5, "Sesto", "Indirizzo 6", 6.30f, null));
        dataSource.add(new Restaurant(6, "Settimo", "Indirizzo 7", 8.30f, null));
        dataSource.add(new Restaurant(7, "Ottavo", "Indirizzo 8", 13.40f, null));
        dataSource.add(new Restaurant(8, "Nono", "Indirizzo 9", 21.89f, null));
        return dataSource;
    }
    */

    public List<Object> getMenuMock() {
        List<Object> dataSource = new ArrayList<>();
        Dish dish;
        //dataSource.add("Primi");
        dish = new Dish("Pasta", 4.99f, "Primo", 3, 0);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Lasagne", 4.99f, "Primo", 4, 1);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Gnocchi", 4.99f, "Primo", 5, 2);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        //dataSource.add("Secondi");
        dish = new Dish("Coscia di pollo", 4.99f, "Secondo", 4, 3);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Uovo sbattuto", 4.99f, "Secondo", 4, 4);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Vitello tonnato", 4.99f, "Secondo", 5, 5);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        //dataSource.add("Bevande");
        dish = new Dish("Acqua", 4.99f, "Bevanda", 2, 6);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Cocacola", 4.99f, "Bevanda", 5, 7);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Fanta", 4.99f, "Bevanda", 5, 8);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Birra", 4.99f, "Bevanda", 2, 9);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        dish = new Dish("Vino", 4.99f, "Bevanda", 5, 10);
        dish.setLiveQuantity(this);
        dataSource.add(dish);
        return dataSource;
    }

    public Dish findInMenuMock(int dishID) {
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

        for (Object dish : dataSource) {
            if (dish instanceof Dish && ((Dish)dish).getID() == dishID) {
                return (Dish) dish;
            }
        }
        return null;
    }
}
