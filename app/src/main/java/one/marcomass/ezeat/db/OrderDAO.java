package one.marcomass.ezeat.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;
import one.marcomass.ezeat.db.entity.DishEntity;

@Dao
public interface OrderDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDish(DishEntity dish);

    @Query("SELECT * FROM order_table WHERE dish_id = :dishID LIMIT 1")
    DishEntity getDishByID(String dishID);

    @Query("UPDATE order_table SET quantity = quantity + 1 WHERE dish_id = :dishID")
    void addDish(String dishID);

    @Query("UPDATE order_table SET quantity = quantity - 1 WHERE dish_id = :dishID AND quantity > 0")
    void removeDish(String dishID);

    @Query("DELETE FROM order_table WHERE dish_id = :dishID")
    void removeAllDish(String dishID);

    @Query("DELETE FROM order_table")
    void removeAll();

    @Query("SELECT * FROM order_table")
    LiveData<List<DishEntity>> getAllDishes();

    @Query("SELECT SUM(quantity * price) FROM order_table")
    LiveData<Float> getTotal();

    @Query("SELECT quantity FROM order_table WHERE dish_id = :dishID LIMIT 1")
    LiveData<Integer> getDishQuantity(String dishID);
}
