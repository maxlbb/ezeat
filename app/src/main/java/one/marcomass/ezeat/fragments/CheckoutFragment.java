package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import one.marcomass.ezeat.RecyclerViewEmpty;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.adapaters.CheckoutAdapter;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Menu;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class CheckoutFragment extends Fragment implements CheckoutAdapter.CartManager {

    private RecyclerViewEmpty recyclerCheckout;
    private CheckoutAdapter checkoutAdapter;
    private MainViewModel mainViewModel;
    private ImageButton buttonExpand;
    private TextView textUpTotal;
    private TextView textTitle;
    private TextView textTotal;
    private TextView textMinOrder;
    private Button buttonOrder;

    private String restaurantName;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.bottom_sheet_checkout, container, false);

        textTotal = view.findViewById(R.id.text_checkout_total);
        textUpTotal = view.findViewById(R.id.text_checkout_total_up);
        textTitle = view.findViewById(R.id.text_checkout_title);
        textMinOrder = view.findViewById(R.id.text_checkout_min_order);
        buttonOrder = view.findViewById(R.id.button_checkout_order);

        buttonExpand = view.findViewById(R.id.button_checkout_expand);
        buttonExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainViewModel.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_COLLAPSED) {
                    mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    mainViewModel.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        mainViewModel.getBottomSheetState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        buttonExpand.setImageResource(getResources()
                                .getIdentifier("@drawable/ic_expand_up", null, getActivity().getPackageName()));
                        textUpTotal.setVisibility(View.VISIBLE);
                        textTitle.setText("Totale");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        buttonExpand.setImageResource(getResources()
                                .getIdentifier("@drawable/ic_expand_down", null, getActivity().getPackageName()));
                        textUpTotal.setVisibility(View.GONE);
                        if (restaurantName == null) {
                            textTitle.setText("Carrello");
                        } else {
                            textTitle.setText(restaurantName);
                        }
                        break;
                }
            }
        });

        recyclerCheckout = view.findViewById(R.id.recycler_checkout);
        recyclerCheckout.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerCheckout.setEmptyView(view.findViewById(R.id.empty_view_checkout));

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
            public void onChanged(Float total) {
                if (total != null) {
                    textTotal.setText(String.format("%.2f", total) + " €");
                    textUpTotal.setText(String.format("%.2f", total) + " €");
                    if (total >= mainViewModel.getMinOrder().getValue()) {
                        buttonOrder.setEnabled(true);
                    } else {
                        buttonOrder.setEnabled(false);
                    }
                } else {
                    textTotal.setText("0.00 €");
                    textUpTotal.setText("0.00 €");
                    buttonOrder.setEnabled(false);
                }
            }
        });

        mainViewModel.getCurrentRestaurantID().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String id) {
                Log.d("viewmodel", id + " id");
                setRestaurantName(id);
            }
        });

        mainViewModel.getMinOrder().observe(this, new Observer<Float>() {
            @Override
            public void onChanged(Float minOrder) {
                textMinOrder.setText("Ordine minimo: " + String.format("%.2f", minOrder) + "€");
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

    public void setRestaurantName(String id) {
        if (id != null) {
            mainViewModel.getRestaurantMenu(id).observe(this, new Observer<Menu>() {
                @Override
                public void onChanged(Menu menu) {
                    restaurantName = menu.getName();
                    if (mainViewModel.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_EXPANDED) {
                        textTitle.setText(restaurantName);
                    }
                }
            });
        } else {
            if (mainViewModel.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_EXPANDED) {
                textTitle.setText("Carrello");
                restaurantName = null;
            }
        }
    }
}
