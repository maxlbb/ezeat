package one.marcomass.ezeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.activities.MainActivity;
import one.marcomass.ezeat.adapaters.RestaurantsGridAdapter;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.SwitchPage;
import one.marcomass.ezeat.adapaters.RestaurantsAdapter;
import one.marcomass.ezeat.models.Restaurant;

public class RestaurantFragment extends Fragment implements SwitchPage {

    private TextView textHeader;
    private RecyclerView recyclerRestaurants;
    private RestaurantsAdapter adapterRestaurants;
    private RestaurantsGridAdapter restaurantsGridAdapter;
    private GridLayoutManager gridLayoutManager;
    private MainViewModel mainVM;

    private List<Restaurant> restaurantsList = new ArrayList<>();

    private RestaurantSelector restListener;

    private boolean gridMenu = true;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            restListener = (RestaurantSelector) context;
        } catch (ClassCastException exception) {
            throw new ClassCastException(context.toString() + " must implement RestaurantSelector");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);

        ActionBar toolbar = ((MainActivity) getActivity()).getSupportActionBar();
        toolbar.setTitle("Ezeat");
        toolbar.setDisplayHomeAsUpEnabled(false);

        recyclerRestaurants = rootView.findViewById(R.id.recycler_main_restaurants);
        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        adapterRestaurants = new RestaurantsAdapter(restaurantsList, restListener);
        restaurantsGridAdapter = new RestaurantsGridAdapter(restaurantsList, restListener);
        recyclerRestaurants.setLayoutManager(gridLayoutManager);
        recyclerRestaurants.setAdapter(adapterRestaurants);
        switchLayout(!gridMenu);

        textHeader = rootView.findViewById(R.id.text_restaurant_header);

        mainVM.getRestaurantList().observe(this, new Observer<List<Restaurant>>() {
            @Override
            public void onChanged(List<Restaurant> restaurants) {
                if (restaurantsList.size() == 0) {
                    restaurantsList.addAll(restaurants);
                    adapterRestaurants.notifyDataSetChanged();
                    restaurantsGridAdapter.notifyDataSetChanged();
                }
                textHeader.setText("Tutti i ristoranti (" + restaurantsList.size() + ")");
            }
        });

        return rootView;
    }

    @Override
    public void switchPage(Restaurant restaurant) {
        //mainVM.setSelectRestaurant(restaurant);
    }

    public interface RestaurantSelector {
        void selectRestaurant(Restaurant restaurant);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        if (gridMenu) {
            menu.findItem(R.id.action_grid).setVisible(true);
            menu.findItem(R.id.action_linear).setVisible(false);
        } else {
            menu.findItem(R.id.action_grid).setVisible(false);
            menu.findItem(R.id.action_linear).setVisible(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //TODO check instance state instead
        if (menu.findItem(R.id.action_grid) == null) {
            inflater.inflate(R.menu.restaurants_menu, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_grid:
                gridMenu = false;
                switchLayout(true);
                getActivity().invalidateOptionsMenu();
                return true;
            case R.id.action_linear:
                gridMenu = true;
                switchLayout(false);
                getActivity().invalidateOptionsMenu();
                return true;
            default:
                return false;

        }
    }

    private void switchLayout(boolean toGrid) {
        if (toGrid) {
            recyclerRestaurants.setAdapter(restaurantsGridAdapter);
            gridLayoutManager.setSpanCount(2);
        } else {
            gridLayoutManager.setSpanCount(1);
            recyclerRestaurants.setAdapter(adapterRestaurants);
        }
    }
}
