package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuHolder> {

    public List<Dish> dataSet;
    private OrderManager listener;

    public MenuAdapter(List<Dish> dataSet, OrderManager listener) {
        this.dataSet = dataSet;
        this.listener = listener;
    }

    public void setDataSet(List<Dish> newDataSet) {
        this.dataSet = newDataSet;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        holder.bindDish(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        if (dataSet != null) {
            return dataSet.size();
        }
        return 0;
    }

    public class MenuHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPrice;
        private TextView textDishCount;
        private ImageButton buttonAddDish;
        private ImageButton buttonRemoveDish;
        private ImageView imageDish;

        public MenuHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_dish_name);
            textPrice = view.findViewById(R.id.text_dish_price);
            textDishCount = view.findViewById(R.id.text_dish_quantity);
            buttonAddDish = view.findViewById(R.id.button_add_dish);
            buttonRemoveDish = view.findViewById(R.id.button_remove_dish);
            imageDish = view.findViewById(R.id.image_dish);

            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dish dish = dataSet.get(getAdapterPosition());
                    listener.addDish(new DishEntity(0, dish.getID(), 1, dish.getPrice(), dish.getName(), dish.getRestaurant()));
                }
            });

            buttonRemoveDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.removeDish(dataSet.get(getAdapterPosition()).getID());
                }
            });

        }

        public void bindDish(final Dish dish) {
            textName.setText(dish.getName());
            textPrice.setText(String.format("%.2f", dish.getPrice()) + "");
            textDishCount.setText(dish.getQuantity() + "");
            Picasso.get().load(dish.getImageUrl()).into(imageDish);
        }
    }

    public interface OrderManager {
        void addDish(DishEntity dish);
        void removeDish(String dishID);
    }
}
