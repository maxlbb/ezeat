package one.marcomass.ezeat;


import java.util.List;

import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.models.Restaurant;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RestaurantAPI {

    String BASE_URL = "http://138.68.86.70/";

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}")
    Call<Menu> getRestaurantsMenu(@Path("id") String menuId);
}
