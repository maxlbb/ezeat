package one.marcomass.ezeat.viewmodels;

import android.app.Application;
import android.util.Log;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.repos.BackendRepository;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainViewModel extends AndroidViewModel {
    private MutableLiveData<Integer> bottomSheetOpen;
    private MutableLiveData<Boolean> isLogged;

    private LiveData<List<Restaurant>> restaurants;

    private BackendRepository backendRepository;

    public MainViewModel(Application application) {
        super(application);

        bottomSheetOpen = new MutableLiveData<>();
        bottomSheetOpen.setValue(BottomSheetBehavior.STATE_HIDDEN);

        backendRepository = BackendRepository.getInstance();
        restaurants = backendRepository.getRestaurants();

        isLogged = new MutableLiveData<>();

    }

    public LiveData<Menu> getRestaurantMenu(String restaurantID) {
        return backendRepository.getRestaurantMenu(restaurantID);
    }

    public LiveData<List<Restaurant>> getRestaurants() {
        return restaurants;
    }

    public LiveData<Integer> getBottomSheetState() {
        return bottomSheetOpen;
    }

    public void setBottomSheetState(int state) {
        bottomSheetOpen.setValue(state);
    }

    public LiveData<Boolean> getIsLogged() {
        return isLogged;
    }

    public void setIsLogged(Boolean isLogged) {
        this.isLogged.setValue(isLogged);
    }

}
