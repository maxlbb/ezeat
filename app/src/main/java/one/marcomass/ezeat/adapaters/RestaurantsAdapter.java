package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantHolder> {

    public final List<Restaurant> restaurantList;
    private final RestaurantFragment.RestaurantSelector restListener;

    public RestaurantsAdapter(List<Restaurant> restaurantList, RestaurantFragment.RestaurantSelector restListener) {
        this.restaurantList = restaurantList;
        //this.restaurantList.add(0, new Restaurant(0, "Tutti i ristoranti", "", 0, ""));
        this.restListener = restListener;
    }

    public class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textAddress;
        private TextView textMinOrder;
        private ImageView imageLogo;

        public RestaurantHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_restaurant_name);
            textAddress = view.findViewById(R.id.text_restaurant_address);
            textMinOrder = view.findViewById(R.id.text_restaurant_min_price);
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
            textAddress.setText(restaurant.getAddress());
            textMinOrder.setText(restaurant.getMinOrder() + " min");
            /*
            //TODO implementing custom animation, remove library dependency - DA RIVALUTARE
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ElasticAnimation.Builder().setView(rootView)
                            .setScaleX(0.85f).setScaleY(0.85f).setDuration(200).doAction();
                    switchPageListener.switchPage(new Restaurant(0,"Cliccato", "finto"));
                }
            });*/
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
        holder.bindData(restaurantList.get(position));
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
