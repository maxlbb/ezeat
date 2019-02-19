package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.adapaters.CheckoutAdapter;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class CheckoutFragment extends Fragment implements CheckoutAdapter.CartManager {

    private RecyclerView recyclerCheckout;
    private CheckoutAdapter checkoutAdapter;
    private MainViewModel mainViewModel;
    private ImageButton buttonExpand;
    private TextView textUpTotal;
    private TextView textTitle;
    private TextView textTotal;

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

        textTotal = view.findViewById(R.id.text_checkout_total);
        textUpTotal = view.findViewById(R.id.text_checkout_total_up);
        textTitle = view.findViewById(R.id.text_checkout_riepilogo);

        buttonExpand = view.findViewById(R.id.button_checkout_expand);
        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (open) {
                    open = false;
                    mainViewModel.setBottomSheetOpen(BottomSheetBehavior.STATE_COLLAPSED);
                    buttonExpand.setImageResource(getResources()
                            .getIdentifier("@drawable/ic_expand_up", null, getActivity().getPackageName()));
                    textUpTotal.setVisibility(View.VISIBLE);
                    textTitle.setText("Totale");
                } else {
                    open = true;
                    mainViewModel.setBottomSheetOpen(BottomSheetBehavior.STATE_EXPANDED);
                    buttonExpand.setImageResource(getResources()
                            .getIdentifier("@drawable/ic_expand_down", null, getActivity().getPackageName()));
                    textUpTotal.setVisibility(View.GONE);
                    textTitle.setText("Carrello");
                }
            }
        });

        recyclerCheckout = view.findViewById(R.id.recycler_checkout);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(getContext()));

        final CheckoutAdapter.CartManager listener = this;
        mainViewModel.getCartAllDishes().observe(this, new Observer<List<DishEntity>>() {
            @Override
            public void onChanged(List<DishEntity> dishEntities) {
                if (dishEntities != null) {
                    if (checkoutAdapter == null) {
                        checkoutAdapter = new CheckoutAdapter(dishEntities, listener);
                        recyclerCheckout.setAdapter(checkoutAdapter);
                    } else {
                        checkoutAdapter.setDataSet(dishEntities);
                    }
                }
            }
        });
        mainViewModel.getCartTotal().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float aFloat) {
                if (aFloat != null) {
                    textTotal.setText(String.format("%.2f", aFloat) + " €");
                    textUpTotal.setText(String.format("%.2f", aFloat) + " €");
                } else {
                    textTotal.setText("0 €");
                    textUpTotal.setText("0 €");
                }
            }
        });

        return view;
    }

    @Override
    public void addDish(DishEntity dish) {
        mainViewModel.addDishToCart(dish);
    }

    @Override
    public void removeDish(String dishID) {
        mainViewModel.removeDishFromCart(dishID);
    }

    @Override
    public void removeAllDish(String dishID) {
        mainViewModel.removeAllDishFromCart(dishID);
    }
}
