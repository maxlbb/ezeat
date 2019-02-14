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


public class RestaurantsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int HEADER_ID = 100;
    public static final int RESTAURANT_ID = 101;

    public final List<Restaurant> restaurantList;
    private final RestaurantFragment.RestaurantSelector restListener;

    public RestaurantsAdapter(List<Restaurant> restaurantList, RestaurantFragment.RestaurantSelector restListener) {
        this.restaurantList = restaurantList;
        this.restaurantList.add(0, new Restaurant(0, "Tutti i ristoranti", "", 0, ""));
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

    public class HeaderHolder extends RecyclerView.ViewHolder {

        private TextView textHeader;

        public HeaderHolder(@NonNull View itemView) {
            super(itemView);
            textHeader = itemView.findViewById(R.id.text_restaurant_header);
        }

        public void bindData(String title) {
            this.textHeader.setText(title + " (" + restaurantList.size() + ")");
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        if (viewType == HEADER_ID) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.header_restaurant, parent, false);
            holder = new HeaderHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_restaurant, parent, false);
            holder = new RestaurantHolder(view);
        }
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == RESTAURANT_ID) {
            ((RestaurantHolder) holder).bindData(restaurantList.get(position));
        } else {
            ((HeaderHolder) holder).bindData(restaurantList.get(position).getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return HEADER_ID;
        }
        return RESTAURANT_ID;
    }

    @Override
    public int getItemCount() {
        return restaurantList.size();
    }
}
