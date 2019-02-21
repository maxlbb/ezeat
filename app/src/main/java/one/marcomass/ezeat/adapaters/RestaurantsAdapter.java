package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantHolder> {

    public final List<Restaurant> dataSet;
    private final RestaurantFragment.RestaurantSelector restListener;

    public RestaurantsAdapter(List<Restaurant> dataSet, RestaurantFragment.RestaurantSelector restListener) {
        this.dataSet = dataSet;
        this.restListener = restListener;
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAddress;
        private TextView textMinOrder;
        private ImageView imageLogo;
        private RatingBar ratingBar;

        public RestaurantHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_restaurant_name);
            textAddress = view.findViewById(R.id.text_restaurant_address);
            textMinOrder = view.findViewById(R.id.text_restaurant_min_price);
            imageLogo = view.findViewById(R.id.image_restaurant_logo);
            ratingBar = view.findViewById(R.id.rating_restaurant);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restListener.selectRestaurant(dataSet.get(getAdapterPosition()).getID());
                }
            });

        }

        public void bindData(Restaurant restaurant) {
            textName.setText(restaurant.getName());
            textAddress.setText(restaurant.getAddress());
            textMinOrder.setText("€ " + restaurant.getMinOrder() + " min");
            ratingBar.setRating(restaurant.getRating() / 10f);
            Picasso.get().load(restaurant.getImageURL()).into(imageLogo);
        }
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        RestaurantHolder holder = new RestaurantHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantHolder holder, int position) {
        holder.bindData(dataSet.get(position));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}
