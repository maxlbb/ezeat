package one.marcomass.ezeat.viewmodels;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.Transformations;
import one.marcomass.ezeat.BackendAPI;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.models.SignResponse;
import one.marcomass.ezeat.repos.CartRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> bottomSheetOpen;
    private MutableLiveData<Float> minOrder;

    private MutableLiveData<List<Restaurant>> restaurantList;

    private CartRepository cartRepository;
    private LiveData<Integer> cartAllDishCount;
    private LiveData<Float> cartTotal;
    private LiveData<List<DishEntity>> cartAllDishes;

    private BackendAPI backendAPI;

    private MutableLiveData<Boolean> isLogged;

    private LiveData<String> currentRestaurantID;

    public MainViewModel(Application application) {
        super(application);
        cartRepository = new CartRepository(application);

        cartAllDishCount = Transformations.map(cartRepository.getAllDishes(), new Function<List<DishEntity>, Integer>() {
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
        bottomSheetOpen.setValue(BottomSheetBehavior.STATE_HIDDEN);
        minOrder = new MutableLiveData<>();
        minOrder.setValue(0f);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackendAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendAPI = retrofit.create(BackendAPI.class);

        isLogged = new MutableLiveData<>();


        cartAllDishes = cartRepository.getAllDishes();

        currentRestaurantID = new MutableLiveData<>();
        currentRestaurantID = Transformations.map(getCartAllDishes(), new Function<List<DishEntity>, String> () {
            @Override
            public String apply(List<DishEntity> input) {
                if (input != null && input.size() > 0) {
                    return input.get(0).getRestaurantID();
                }
                Log.d("viewmodel", "null id");
                return null;
            }
        });

    }

    //Cart management ------------------------------------------------------------------------------

    public LiveData<List<DishEntity>> getCartAllDishes() {
        return cartAllDishes;
    }

    public boolean addDishToCart(DishEntity dish) {
        //TODO find a better way to check if is from a different restaurant
        if (currentRestaurantID.getValue() == null) {
            cartRepository.add(dish);
            return true;
        } else if (cartAllDishes.getValue().get(0).getRestaurantID().equals(dish.getRestaurantID())) {
            cartRepository.add(dish);
            return true;
        } else {
            return false;
        }
    }

    public void removeDishFromCart(String dishID) {
        cartRepository.removeDish(dishID);
    }

    public void removeAllDishFromCart(String dishID) { cartRepository.removeAllDish(dishID); }

    public void removeAllFromCart() {
        cartRepository.removeAll();
    }

    public LiveData<Integer> getCartAllDishCount() {
        return cartAllDishCount;
    }

    public LiveData<Integer> getDishQuantity(String dishID) {
        return cartRepository.getDishQuantity(dishID);
    }

    public LiveData<Float> getCartTotal() {
        return cartTotal;
    }

    //TODO API or something from repo
    public LiveData<Dish> getDishFromID(int dishID) {
        MutableLiveData<Dish> dish = new MutableLiveData<>();
        //dish.setValue(findInMenuMock(dishID));
        return dish;
    }

    //----------------------------------------------------------------------------------------------

    public LiveData<Integer> getBottomSheetState() {
        return bottomSheetOpen;
    }

    public void setBottomSheetState(int state) {
        bottomSheetOpen.setValue(state);
    }

    public LiveData<Float> getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(float newMinOrder) {
        minOrder.setValue(newMinOrder);
    }

    public LiveData<Menu> getRestaurantMenu(String restaurantID) {
        return loadMenu(restaurantID);
    }

    public LiveData<List<Restaurant>> getRestaurantList() {
        if (restaurantList == null) {
            restaurantList = new MutableLiveData<>();
            loadRestaurants();
        }
        return restaurantList;
    }

    private void loadRestaurants() {
        Call<List<Restaurant>> call = backendAPI.getRestaurants();

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

    private LiveData<Menu> loadMenu(String restaurantID) {
        Call<Menu> call = backendAPI.getRestaurantsMenu(restaurantID);

        final MutableLiveData<Menu> responseMenu = new MutableLiveData<>();

        call.enqueue(new Callback<Menu>() {
            @Override
            public void onResponse(Call<Menu> call, Response<Menu> response) {
                responseMenu.setValue(response.body());
            }

            @Override
            public void onFailure(Call<Menu> call, Throwable t) {

            }
        });

        return responseMenu;
    }

    public LiveData<SignResponse> register(String username, String password, String email) {
        Call<SignResponse> call = backendAPI.register(username, password, email);

        final MutableLiveData<SignResponse> signResponse = new MutableLiveData<>();

        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                signResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {

            }
        });

        return signResponse;
    }

    public LiveData<SignResponse> login(String identifier, String password) {
        Call<SignResponse> call = backendAPI.login(identifier, password);

        final MutableLiveData<SignResponse> signResponse = new MutableLiveData<>();

        call.enqueue(new Callback<SignResponse>() {
            @Override
            public void onResponse(Call<SignResponse> call, Response<SignResponse> response) {
                signResponse.setValue(response.body());
            }

            @Override
            public void onFailure(Call<SignResponse> call, Throwable t) {

            }
        });

        return signResponse;
    }

    public LiveData<Boolean> getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(Boolean isLogged) {
        this.isLogged.setValue(isLogged);
    }

    public LiveData<String> getCurrentRestaurantID() {
        return currentRestaurantID;
    }
}
