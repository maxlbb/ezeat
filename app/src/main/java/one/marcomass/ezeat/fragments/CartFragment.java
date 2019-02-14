package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.adapaters.CartAdapter;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;

public class CartFragment extends Fragment {

    private TextView textCount;
    private Button buttonCart;
    private RecyclerView recyclerView;
    private CartAdapter cartAdapter;
    private MainViewModel mainViewModel;

    private CartFragment context;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        context = this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        textCount = rootView.findViewById(R.id.text_count_cart);

        recyclerView = rootView.findViewById(R.id.recycler_cart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        cartAdapter = new CartAdapter(new ArrayList<Dish>(), mainViewModel);
        recyclerView.setAdapter(cartAdapter);
        mainViewModel.getCartAllDishes().observe(this, new Observer<List<DishEntity>>() {
            @Override
            public void onChanged(List<DishEntity> dishEntities) {
                final List<Dish> newDataset = new ArrayList<>();
                int count = 0;
                for (int i = 0; i < dishEntities.size(); i++) {
                    final int pos = i;
                    final DishEntity entity = dishEntities.get(pos);
                    mainViewModel.getDishFromID(entity.getDishID()).observe(context, new Observer<Dish>() {
                        @Override
                        public void onChanged(final Dish dish) {
                            if (dish != null) {
                                newDataset.add(dish);
                                dish.setLiveQuantity(mainViewModel);
                                dish.getLiveQuantity().observe(getActivity(), new Observer<Integer>() {
                                    @Override
                                    public void onChanged(Integer integer) {
                                        if (integer != null) {
                                            dish.setQuantity(integer);
                                            cartAdapter.notifyItemChanged(pos);
                                        }
                                    }
                                });
                                cartAdapter.setDataset(newDataset);
                            }
                        }
                    });
                    count += entity.getQuantity();
                }
                textCount.setText(count + " oggetti nel carrello");
            }
        });

        buttonCart = rootView.findViewById(R.id.button_empty_cart);
        buttonCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainViewModel.removeAllFromCart();
            }
        });

        return rootView;
    }
}
