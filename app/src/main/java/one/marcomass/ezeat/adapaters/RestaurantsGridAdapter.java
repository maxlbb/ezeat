package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;


public class RestaurantsGridAdapter extends RecyclerView.Adapter<RestaurantsGridAdapter.RestaurantHolder> {

    public final List<Restaurant> restaurantList;
    private final RestaurantFragment.RestaurantSelector restListener;

    public RestaurantsGridAdapter(List<Restaurant> restaurantList, RestaurantFragment.RestaurantSelector restListener) {
        this.restaurantList = restaurantList;
        this.restListener = restListener;
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private ImageView imageLogo;

        public RestaurantHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_restaurant_name);
            imageLogo = view.findViewById(R.id.image_restaurant_logo);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restListener.selectRestaurant(restaurantList.get(getAdapterPosition()));
                }
            });

        }

        public void bindData(Restaurant restaurant) {
            textName.setText(restaurant.getName());
            Picasso.get().load(restaurant.getImageURL()).into(imageLogo);
        }
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant_grid, parent, false);
        RestaurantHolder holder = new RestaurantHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        holder.bindData(restaurantList.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}

