package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.ContentLoadingProgressBar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.adapaters.MenuAdapter;

public class MenuFragment extends Fragment implements MenuAdapter.CartManager {

    private RecyclerView recyclerMenu;
    private MainViewModel mainVM;
    private MenuAdapter menuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ContentLoadingProgressBar loadingProgressBar;
    private String restaurantID;

    private ImageView imageLogo;
    private TextView textMin;

    private boolean loaded = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainVM = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

        if (loaded) {
            mainVM.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }

        if (getArguments() != null) {
            restaurantID = getArguments().getString(Util.RESTAURANT_ID, "noID");
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        imageLogo = rootView.findViewById(R.id.image_restaurant_logo);
        //textMin = rootView.findViewById(R.id.text_restaurant_min_price);
        loadingProgressBar = rootView.findViewById(R.id.progress_menu);
        recyclerMenu = rootView.findViewById(R.id.recycler_menu);
        linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerMenu.setLayoutManager(linearLayoutManager);
        ((SimpleItemAnimator) recyclerMenu.getItemAnimator()).setSupportsChangeAnimations(false);
        menuAdapter = new MenuAdapter(null, this);
        recyclerMenu.setAdapter(menuAdapter);

        final MenuFragment lifecycle = this;

        mainVM.getRestaurantMenu(restaurantID).observe(this, new Observer<Menu>() {
            @Override
            public void onChanged(final Menu menu) {
                if (menu.getProducts() != null) {
                    for (int i = 0; i < menu.getProducts().size(); i++) {
                        final int pos = i;
                        menu.getProducts().get(pos).setLiveQuantity(mainVM).observe(lifecycle, new Observer<Integer>() {
                            @Override
                            public void onChanged(Integer integer) {
                                menu.getProducts().get(pos).setQuantity(integer == null ? 0 : integer);
                                menuAdapter.notifyItemChanged(pos);
                            }
                        });
                    }
                    menuAdapter.setDataSet(menu.getProducts());
                    recyclerMenu.setVisibility(View.VISIBLE);
                    Picasso.get().load(menu.getImage_url()).into(imageLogo);
                    loaded = true;
                    mainVM.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                    mainVM.setMinOrder(menu.getMinOrder());
                    //textMin.setText("Ordine minimo: " + menu.getMinOrder() + "€");
                }
            }
        });
        return rootView;
    }

    @Override
    public void onPause() {
        mainVM.setBottomSheetState(BottomSheetBehavior.STATE_HIDDEN);
        super.onPause();
    }

    @Override
    public void onResume() {
        if (loaded) {
            mainVM.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
        }
        super.onResume();
    }

    @Override
    public void addDish(DishEntity dish) {
        mainVM.addDishToCart(dish);
    }

    @Override
    public void removeDish(String dishID) {
        mainVM.removeDishFromCart(dishID);
    }
}

