package one.marcomass.ezeat.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import one.marcomass.ezeat.CartInterface;
import one.marcomass.ezeat.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;

public class CartFragment extends Fragment {

    private TextView textCount;
    private TextView textList;
    private Button buttonCart;
    private MainViewModel mainViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_cart, container, false);

        textCount = rootView.findViewById(R.id.text_count_cart);
        mainViewModel.getCartDishCount().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                textCount.setText("Hai inserito " + integer + " oggetti");
            }
        });

        textList = rootView.findViewById(R.id.text_list);
        mainViewModel.getCartAllDishes().observe(this, new Observer<List<DishEntity>>() {
            @Override
            public void onChanged(List<DishEntity> dishEntities) {
                textList.setText(dishEntities.toString());
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
