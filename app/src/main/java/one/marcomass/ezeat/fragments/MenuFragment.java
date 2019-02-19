package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import one.marcomass.ezeat.CartInterface;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.adapaters.MenuAdapter;
import one.marcomass.ezeat.models.Dish;

public class MenuFragment extends Fragment implements CartInterface {

    private RecyclerView recyclerMenu;
    private MainViewModel mainVM;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ContentLoadingProgressBar loadingProgressBar;
    private MenuFragment thisInt;

    private boolean first = true;

    private String restaurantID;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        if (getArguments() != null) {
            restaurantID = getArguments().getString(Util.RESTAURANT_ID, "noID");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        loadingProgressBar = rootView.findViewById(R.id.progress_menu);
        recyclerMenu = rootView.findViewById(R.id.recycler_menu);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(linearLayoutManager);

        thisInt = this;

        final float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 120, getResources().getDisplayMetrics());

        mainVM.getRestaurantMenu().observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(List<Object> objects) {
                if (objects != null) {
                    if (menuAdapter == null) {
                        menuAdapter = new MenuAdapter(objects, thisInt);
                        ((SimpleItemAnimator) recyclerMenu.getItemAnimator()).setSupportsChangeAnimations(false);
                        for (int i = 0; i < objects.size(); i++) {
                            final int pos = i;
                            if (objects.get(i) instanceof Dish) {
                                final Dish dish = (Dish) objects.get(i);
                                //TODO temp getActivity
                                dish.getLiveQuantity().observe(getActivity(), new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        if (integer != null) {
                                            dish.setQuantity(integer);
                                            menuAdapter.notifyItemChanged(pos);
                                        } else {
                                            dish.setQuantity(0);
                                            menuAdapter.notifyItemChanged(pos);
                                        }
                                    }
                                });
                            }
                        }
                        recyclerMenu.setAdapter(menuAdapter);
                    } else {
                        for (int i = 0; i < objects.size(); i++) {
                            final int pos = i;
                            if (objects.get(i) instanceof Dish) {
                                final Dish dish = (Dish) objects.get(i);
                                //TODO temp getActivity
                                dish.getLiveQuantity().observe(getActivity(), new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        dish.setQuantity(integer);
                                        menuAdapter.notifyItemChanged(pos);
                                    }
                                });
                            }
                        }
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

