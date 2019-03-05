package one.marcomass.ezeat.adapaters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.db.entity.DishEntity;

public class CheckoutAdapter extends ListAdapter<DishEntity, CheckoutAdapter.DishHolder> {

    private OrderManager orderListener;

    public CheckoutAdapter(OrderManager orderListener) {
        super(new DiffUtil.ItemCallback<DishEntity>() {
            @Override
            public boolean areItemsTheSame(@NonNull DishEntity oldItem, @NonNull DishEntity newItem) {
                return oldItem.getDishID().equals(newItem.getDishID());
            }

            @Override
            public boolean areContentsTheSame(@NonNull DishEntity oldItem, @NonNull DishEntity newItem) {
                return oldItem.getQuantity() == newItem.getQuantity();
            }
        });
        this.orderListener = orderListener;
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
                    orderListener.addDish(getItem(getAdapterPosition()));
                }
            });

            buttonRemoveDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListener.removeDish(getItem(getAdapterPosition()).getDishID());
                }
            });

            buttonRemoveAllDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    orderListener.removeAllDish(getItem(getAdapterPosition()).getDishID());
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
        holder.bindData(getItem(position));
    }

    public interface OrderManager {
        void addDish(DishEntity dish);
        void removeDish(String dishID);
        void removeAllDish(String dishID);
    }
}
