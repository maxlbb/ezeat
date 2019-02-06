package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.CartInterface;
import one.marcomass.ezeat.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.adapaters.MenuAdapter;
import one.marcomass.ezeat.models.Cart;
import one.marcomass.ezeat.models.Dish;

public class MenuFragment extends Fragment implements CartInterface {

    private RecyclerView recyclerMenu;
    private MainViewModel mainVM;
    private MenuAdapter menuAdapter;
    private ContentLoadingProgressBar loadingProgressBar;
    private MenuFragment thisInt;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        loadingProgressBar = rootView.findViewById(R.id.progress_menu);
        recyclerMenu = rootView.findViewById(R.id.recycler_menu);
        recyclerMenu.setLayoutManager(new LinearLayoutManager(getContext()));
        thisInt = this;

        final float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());

        mainVM.getRestaurantMenu().observe(this, new Observer<ArrayList<Object>>() {
            @Override
            public void onChanged(ArrayList<Object> objects) {
                if (objects != null) {
                    if (menuAdapter == null) {
                        menuAdapter = new MenuAdapter(objects, thisInt, dp);
                        recyclerMenu.setAdapter(menuAdapter);
                    } else {
                        menuAdapter.setDataSet(objects);
                    }
                    loadingProgressBar.setVisibility(View.GONE);
                    recyclerMenu.setVisibility(View.VISIBLE);
                }
            }
        });

        return rootView;
    }

    @Override
    public void addToCart(Dish dish) {
        mainVM.addDishToCart(dish);
    }

    public MainViewModel getMainViewModel() {
        return mainVM;
    }
}

