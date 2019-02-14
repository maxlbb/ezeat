package one.marcomass.ezeat.adapaters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.models.Dish;

public class MenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public List<Object> dataSet;
    //TODO refactor with interface
    private MenuFragment menuFragment;

    public MenuAdapter(List<Object> menu, MenuFragment cart) {
        this.dataSet = menu;
        this.menuFragment = cart;
    }

    public void setDataSet(List<Object> objects) {
        this.dataSet = objects;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_dish, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final MenuHolder menuHolder = (MenuHolder) holder;
        menuHolder.bindDish((Dish) dataSet.get(position), menuFragment);
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }

    public class MenuHolder extends RecyclerView.ViewHolder {

        private TextView textName;
        private TextView textPrice;
        private TextView textDishCount;
        private ImageButton buttonAddDish;
        private ImageButton buttonRemoveDish;

        public MenuHolder(View view) {
            super(view);
            textName = view.findViewById(R.id.text_dish_name);
            textPrice = view.findViewById(R.id.text_dish_price);
            textDishCount = view.findViewById(R.id.text_dish_quantity);
            buttonAddDish = view.findViewById(R.id.button_add_dish);
            buttonRemoveDish = view.findViewById(R.id.button_remove_dish);

            //TODO refactoring
            /*
            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final Dish dish = (Dish) dataSet.get(getAdapterPosition());
                    menuFragment.addToCart((Dish) dataSet.get(getAdapterPosition()));
                    if (dish.getQuantity() == 0) {
                        animating = true;
                        ValueAnimator widthAnimator = ValueAnimator.ofInt(height, (height / 12) * 26);
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
                                animating = false;
                                super.onAnimationEnd(animation);
                            }
                        });
                        widthAnimator.setDuration(300);
                        widthAnimator.start();
                    }
                }
            });
            */

            buttonAddDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dish dish = (Dish) dataSet.get(getAdapterPosition());
                    menuFragment.getMainViewModel().addDishToCart(dish);
                }
            });

            buttonRemoveDish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dish dish = (Dish) dataSet.get(getAdapterPosition());
                    menuFragment.getMainViewModel().removeDishFromCart(dish);
                }
            });

        }

        public void bindDish(final Dish dish, final MenuFragment menuFragment) {
            textName.setText(dish.getName());
            textPrice.setText(dish.getPrice() + "");
            textDishCount.setText(dish.getQuantity() + "");

            /*
            if (!animating) {
                if (dish.getQuantity() > 0) {
                    actionContainer.getLayoutParams().height = (height / 12) * 26;
                    actionContainer.requestLayout();
                    buttonRemoveDish.setVisibility(View.VISIBLE);
                    textDishCount.setVisibility(View.VISIBLE);
                } else {
                    actionContainer.getLayoutParams().height = height;
                    actionContainer.requestLayout();
                    buttonRemoveDish.setVisibility(View.GONE);
                    textDishCount.setVisibility(View.GONE);
                }
            }

            menuFragment.getMainViewModel().getCartDishCount(dish.getID()).observe(menuFragment, new Observer<DishEntity>() {
                @Override
                public void onChanged(DishEntity dishEntity) {
                    if (dishEntity != null) {
                        Log.d("Loggg", dishEntity.getDishID() + " " + dishEntity.getQuantity());
                        textDishCount.setText(dishEntity.getQuantity() + "");
                        if (dishEntity.getQuantity() == 0) {
                            actionContainer.getLayoutParams().height = height;
                            actionContainer.requestLayout();
                            buttonRemoveDish.setVisibility(View.GONE);
                            textDishCount.setVisibility(View.GONE);
                            Log.d("Observing", dish.getName() + "reset");
                        } else if (firstBind) {
                            actionContainer.getLayoutParams().height = (height / 12) * 26;
                            actionContainer.requestLayout();
                            buttonRemoveDish.setVisibility(View.VISIBLE);
                            textDishCount.setVisibility(View.VISIBLE);
                            firstBind = false;
                            Log.d("Observing", dish.getName() + " first bind");
                        }
                    }
                }
            });
            */
        }
    }
}
