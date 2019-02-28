package one.marcomass.ezeat.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;
import one.marcomass.ezeat.repos.OrderRepository;

public class OrderViewModel extends AndroidViewModel {

    private OrderRepository orderRepository;
    private LiveData<Integer> allDishQuantity;
    private LiveData<Float> total;
    private LiveData<List<DishEntity>> allDishes;
    private LiveData<String> orderRestaurantID;
    private MutableLiveData<Float> minimum;

    public OrderViewModel(Application application) {
        super(application);
        orderRepository = OrderRepository.getInstance(application);
        allDishQuantity = Transformations.map(orderRepository.getAllDishes(), new Function<List<DishEntity>, Integer>() {
            @Override
            public Integer apply(List<DishEntity> input) {
                int totalQuantity = 0;
                for (DishEntity dishEntity : input) {
                    totalQuantity += dishEntity.getQuantity();
                }
                return totalQuantity;
            }
        });
        total = orderRepository.getTotal();
        allDishes = orderRepository.getAllDishes();

        orderRestaurantID = new MutableLiveData<>();
        orderRestaurantID = Transformations.map(getAllDishes(), new Function<List<DishEntity>, String>() {
            @Override
            public String apply(List<DishEntity> input) {
                if (input != null && input.size() > 0) {
                    return input.get(0).getRestaurantID();
                }
                return null;
            }
        });

        minimum = new MutableLiveData<>();
        minimum.setValue(0f);
    }

    public LiveData<List<DishEntity>> getAllDishes() {
        return allDishes;
    }

    public boolean addDish(DishEntity dish) {
        //TODO find a better way to check if is from a different restaurant
        if (orderRestaurantID.getValue() == null) {
            orderRepository.add(dish);
            return true;
        } else if (allDishes.getValue().get(0).getRestaurantID().equals(dish.getRestaurantID())) {
            orderRepository.add(dish);
            return true;
        } else {
            return false;
        }
    }

    public void removeDish(String dishID) {
        orderRepository.removeDish(dishID);
    }

    public void removeAllDish(String dishID) {
        orderRepository.removeAllDish(dishID);
    }

    public void removeAll() {
        orderRepository.removeAll();
    }

    public LiveData<Integer> getAllDishQuantity() {
        return allDishQuantity;
    }

    public LiveData<Integer> getDishQuantity(String dishID) {
        return orderRepository.getDishQuantity(dishID);
    }

    public LiveData<Float> getTotal() {
        return total;
    }

    //TODO API or something from repo
    public LiveData<Dish> getDishByID(int dishID) {
        MutableLiveData<Dish> dish = new MutableLiveData<>();
        //dish.setValue(findInMenuMock(dishID));
        return dish;
    }

    public LiveData<String> getOrderRestaurantID() {
        return orderRestaurantID;
    }

    public LiveData<Float> getMinimum() {
        return minimum;
    }

    public void setMinimum(float minimum) {
        this.minimum.setValue(minimum);
    }
}
