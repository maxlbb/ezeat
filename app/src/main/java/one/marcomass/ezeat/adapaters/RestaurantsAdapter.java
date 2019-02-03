package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.skydoves.elasticviews.ElasticAnimation;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.SwitchPage;
import one.marcomass.ezeat.models.Restaurant;


public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsAdapter.RestaurantHolder> {

    private final ArrayList<Restaurant> restaurantList;
    private static SwitchPage switchPageListener;

    public RestaurantsAdapter(ArrayList<Restaurant> restaurantList, SwitchPage switchPageListener) {
        this.restaurantList = restaurantList;
        this.switchPageListener = switchPageListener;
    }

    public static class RestaurantHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textDescription;
        private ImageView imageLogo;
        private View rootView;

        public RestaurantHolder(View view) {
            super(view);
            rootView = view;
            textName = view.findViewById(R.id.text_restaurant_name);
            textDescription = view.findViewById(R.id.text_restaurant_description);
            imageLogo = view.findViewById(R.id.image_restaurant_logo);
        }

        public void bindData(Restaurant restaurant) {
            textName.setText(restaurant.getName());
            textDescription.setText(restaurant.getDescription());
            //TODO implementing custom animation, remove library dependency
            rootView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ElasticAnimation.Builder().setView(rootView)
                            .setScaleX(0.85f).setScaleY(0.85f).setDuration(200).doAction();
                    switchPageListener.switchPage(new Restaurant("Cliccato", "finto"));
                }
            });
        }
    }

    @NonNull
    @Override
    public RestaurantHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_restaurant, parent, false);
        return new RestaurantHolder(view);
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
