package one.marcomass.ezeat.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.SwitchPage;
import one.marcomass.ezeat.adapaters.RestaurantsAdapter;
import one.marcomass.ezeat.models.Restaurant;

public class RestaurantFragment extends Fragment implements SwitchPage {

    private RecyclerView recyclerRestaurants;
    private RestaurantsAdapter adapterRestaurants;
    private LinearLayoutManager linearLayoutManager;
    private MainViewModel mainVM;

    private RestaurantSelector restListener;

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
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_restaurant, container, false);
        recyclerRestaurants = rootView.findViewById(R.id.recycler_main_restaurants);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerRestaurants.setLayoutManager(linearLayoutManager);
        adapterRestaurants = new RestaurantsAdapter(mainVM.getRestaurantMock(), restListener);
        recyclerRestaurants.setAdapter(adapterRestaurants);

        /*DividerItemDecoration divider = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerRestaurants.addItemDecoration(divider);*/
        return rootView;
    }

    @Override
    public void switchPage(Restaurant restaurant) {
        mainVM.setSelectRestaurant(restaurant);
    }

    public interface RestaurantSelector {
        void selectRestaurant(Restaurant restaurant);
    }
}
