package one.marcomass.ezeat.repos;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.models.SignResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class BackendRepository {

    private static BackendRepository INSTANCE = null;

    private BackendAPI backendAPI;

    private BackendRepository() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BackendAPI.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        backendAPI = retrofit.create(BackendAPI.class);
    }

    public static BackendRepository getInstance() {
        if (INSTANCE == null) {
            return new BackendRepository();
        }
        return INSTANCE;
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

    public LiveData<List<Restaurant>> getRestaurants() {
        Call<List<Restaurant>> call = backendAPI.getRestaurants();
        final MutableLiveData<List<Restaurant>> restaurants = new MutableLiveData<>();

        call.enqueue(new Callback<List<Restaurant>>() {
            @Override
            public void onResponse(Call<List<Restaurant>> call, Response<List<Restaurant>> response) {
                restaurants.setValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Restaurant>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        return restaurants;
    }

    public LiveData<Menu> getRestaurantMenu(String restaurantID) {
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

}
