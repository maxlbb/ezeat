package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.models.Dish;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.DishHolder> {

    public List<Dish> dataSet;

    private MainViewModel  mainVM;

    public CartAdapter(List<Dish> cartDishes, MainViewModel mainViewModel) {
        this.dataSet = cartDishes;
        this.mainVM = mainViewModel;
    }

    public void setDataset(List<Dish> cartDishes) {
        this.dataSet = cartDishes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DishHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new DishHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishHolder holder, int position) {
        holder.bindDish(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class DishHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPrice;
        private TextView textDishCount;
        private ImageButton buttonAddDish;
        private ImageButton buttonRemoveDish;

        public DishHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_dish_name);
            textPrice = view.findViewById(R.id.text_dish_price);
            textDishCount = view.findViewById(R.id.text_dish_quantity);
            buttonAddDish = view.findViewById(R.id.button_add_dish);
            buttonRemoveDish = view.findViewById(R.id.button_remove_dish);

            /*
            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainVM.addDishToCart(dataSet.get(getAdapterPosition()).getID());
                }
            });

            buttonRemoveDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainVM.removeDishFromCart(dataSet.get(getAdapterPosition()));
                }
            });
            */
        }

        public void bindDish(final Dish dish) {
            textName.setText(dish.getName());
            textPrice.setText(String.valueOf(dish.getPrice()));
            textDishCount.setText(String.valueOf(dish.getQuantity()));
        }
    }
}
