package one.marcomass.ezeat.adapaters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RatingBar;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.CartInterface;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.models.Cart;
import one.marcomass.ezeat.models.Dish;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Object> dataSet;
    private MenuFragment menuFragment;
    private static float dp;

    public MenuAdapter(ArrayList<Object> menu, MenuFragment cart, float dp) {
        this.dataSet = menu;
        this.menuFragment = cart;
        this.dp = dp;
    }

    public void setDataSet(ArrayList<Object> objects) {
        this.dataSet = objects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder holder;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_category, parent, false);
                holder = new CategoryHolder(view);
                break;
            default:
                view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_dish, parent, false);
                holder = new MenuHolder(view);
        }

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case 0:
                ((CategoryHolder)holder).textCategory.setText((String) dataSet.get(position));
                break;
            default:
                final MenuHolder menuHolder = (MenuHolder) holder;
                menuHolder.bindDish((Dish)dataSet.get(position));
                menuHolder.rootView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        menuFragment.addToCart((Dish) dataSet.get(menuHolder.getAdapterPosition()));
                    }
                });
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (dataSet.get(position) instanceof String) {
            return 0;
        }
        return 1;
    }

    public static class MenuHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPrice;
        private TextView textDishCount;
        private ImageButton buttonAddDish;
        private ImageButton buttonRemoveDish;
        private View actionContainer;
        public View rootView;

        public MenuHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_dish_name);
            textPrice = view.findViewById(R.id.text_dish_price);
            textDishCount = view.findViewById(R.id.text_item_count);
            buttonAddDish = view.findViewById(R.id.button_add_dish);
            buttonRemoveDish = view.findViewById(R.id.button_remove_dish);
            actionContainer = view.findViewById(R.id.frame_action_container);
            rootView = view;
        }

        public void bindDish(Dish dish) {
            textName.setText(dish.getName());
            textPrice.setText(dish.getPrice() + "€");
            //TODO ottimizzare col ViewHolder
            final int width = actionContainer.getLayoutParams().width;
            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ValueAnimator widthAnimator = ValueAnimator.ofInt(width, (width/12)*26);
                    widthAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                        @Override
                        public void onAnimationUpdate(ValueAnimator animation) {
                            actionContainer.getLayoutParams().height = (int) animation.getAnimatedValue();
                            actionContainer.requestLayout();
                        }

                    });
                    widthAnimator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            textDishCount.setVisibility(View.VISIBLE);
                            buttonRemoveDish.setVisibility(View.VISIBLE);
                            super.onAnimationEnd(animation);
                        }
                    });
                    widthAnimator.start();
                }
            });
        }
    }

    public static class CategoryHolder extends RecyclerView.ViewHolder {

        public TextView textCategory;

        public CategoryHolder(View view) {
            super(view);
            textCategory = view.findViewById(R.id.text_category);
        }
    }
}
