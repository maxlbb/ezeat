package one.marcomass.ezeat;


import java.util.List;

import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.models.SignResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface BackendAPI {

    String BASE_URL = "http://138.68.86.70/";

    @GET("restaurants")
    Call<List<Restaurant>> getRestaurants();

    @GET("restaurants/{id}")
    Call<Menu> getRestaurantsMenu(@Path("id") String menuId);

    @FormUrlEncoded
    @POST("auth/local/register")
    Call<SignResponse> register(@Field("username") String username,
                                @Field("password") String password,
                                @Field("email") String email);

    @FormUrlEncoded
    @POST("auth/local/")
    Call<SignResponse> login(@Field("identifier") String identifier,
                                @Field("password") String password);
}
