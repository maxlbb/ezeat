package one.marcomass.ezeat.adapaters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.db.entity.DishEntity;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.DishHolder> {

    public List<DishEntity> dataSet;
    private CartManager cartListener;

    public CheckoutAdapter(List<DishEntity> dishes, CartManager cartListener) {
        this.dataSet = dishes;
        this.cartListener = cartListener;
    }

    public void setDataSet(List<DishEntity> dataSet) {
        this.dataSet = dataSet;
        notifyDataSetChanged();
    }

    public class DishHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPrice;
        private TextView textDishQuantity;
        private ImageButton buttonAddDish;
        private ImageButton buttonRemoveDish;
        private ImageButton buttonRemoveAllDish;

        public DishHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_dish_name);
            textPrice = view.findViewById(R.id.text_dish_price);
            textDishQuantity = view.findViewById(R.id.text_dish_quantity);
            buttonAddDish = view.findViewById(R.id.button_add_dish);
            buttonRemoveDish = view.findViewById(R.id.button_remove_dish);
            buttonRemoveAllDish = view.findViewById(R.id.button_remove_all_dish);

            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartListener.addDish(dataSet.get(getAdapterPosition()));
                }
            });

            buttonRemoveDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartListener.removeDish(dataSet.get(getAdapterPosition()).getDishID());
                }
            });

            buttonRemoveAllDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cartListener.removeAllDish(dataSet.get(getAdapterPosition()).getDishID());
                }
            });
        }

        public void bindData(DishEntity dish) {
            this.textName.setText(dish.getName());
            this.textPrice.setText(String.format("%.2f", dish.getPrice()));
            this.textDishQuantity.setText(String.valueOf(dish.getQuantity()));
        }
    }

    @NonNull
    @Override
    public DishHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish_checkout, parent, false);
        return new DishHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DishHolder holder, int position) {
        holder.bindData(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public interface CartManager {
        void addDish(DishEntity dish);
        void removeDish(String dishID);
        void removeAllDish(String dishID);
    }
}
