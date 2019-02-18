package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.List;
import java.util.concurrent.RecursiveAction;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.adapaters.CheckoutAdapter;
import one.marcomass.ezeat.adapaters.MenuAdapter;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class CheckoutFragment extends Fragment implements CheckoutAdapter.CartManager, Observer<List<DishEntity>> {

    private RecyclerView recyclerCheckout;
    private CheckoutAdapter checkoutAdapter;
    private MainViewModel mainViewModel;
    private ImageButton buttonExpand;

    private boolean open = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_checkout, container, false);
        buttonExpand = view.findViewById(R.id.button_checkout_expand);
        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (open) {
                    open = false;
                    mainViewModel.setBottomSheetOpen(false);
                    buttonExpand.setImageResource(getResources().getIdentifier("@drawable/ic_expand_up", null, getActivity().getPackageName()));
                } else {
                    open = true;
                    mainViewModel.setBottomSheetOpen(true);
                    buttonExpand.setImageResource(getResources().getIdentifier("@drawable/ic_expand_down", null, getActivity().getPackageName()));
                }
            }
        });

        recyclerCheckout = view.findViewById(R.id.recycler_checkout);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(getContext()));

        mainViewModel.getCartAllDishes().observe(this, this);

        return view;
    }

    @Override
    public void onChanged(List<DishEntity> dishEntities) {
        /*if (dishEntities != null) {
            if (checkoutAdapter == null) {
                checkoutAdapter = new CheckoutAdapter()
            }
        }*/
    }

    @Override
    public void addDish(Dish dish) {
        mainViewModel.addDishToCart(dish);
    }

    @Override
    public void removeDish(Dish dish) {
        mainViewModel.removeDishFromCart(dish);
    }

    @Override
    public void removeAllDish(Dish dish) {
        //TODO implement in DAO
    }
}
