package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.SwitchPage;
import one.marcomass.ezeat.adapaters.RestaurantsAdapter;
import one.marcomass.ezeat.models.Restaurant;

public class RestaurantFragment extends Fragment implements SwitchPage {

    private RecyclerView recyclerRestaurants;
    private RestaurantsAdapter adapterRestaurants;
    private MainViewModel mainVM;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurants, container, false);
        recyclerRestaurants = rootView.findViewById(R.id.recycler_main_restaurants);
        recyclerRestaurants.setLayoutManager(new LinearLayoutManager(getContext()));
        adapterRestaurants = new RestaurantsAdapter(mainVM.getRestaurantMock(), this);
        recyclerRestaurants.setAdapter(adapterRestaurants);
        return rootView;
    }

    @Override
    public void switchPage(Restaurant restaurant) {
        mainVM.setSelectRestaurant(restaurant);
    }
}
