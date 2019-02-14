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
public interface CartDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertDish(DishEntity dish);

    @Query("SELECT * FROM cart_table WHERE dish_id = :dishID LIMIT 1")
    DishEntity getDishByID(int dishID);

    @Query("UPDATE cart_table SET quantity = quantity + 1 WHERE dish_id = :dishID")
    void addDish(int dishID);

    //@Query("DELETE FROM cart_table WHERE dish_id LIKE :dishID")
    //@Query("DELETE FROM cart_table WHERE id = (SELECT id FROM cart_table WHERE dish_id = :dishID LIMIT 1)")
    @Query("UPDATE cart_table SET quantity = quantity - 1 WHERE dish_id = :dishID AND quantity > 0")
    void removeDish(int dishID);

    //TODO find better solution
    @Query("UPDATE cart_table SET quantity = 0")
    void removeAll();

    @Query("SELECT * FROM cart_table")
    LiveData<List<DishEntity>> getAllDishes();

    @Query("SELECT quantity FROM cart_table WHERE dish_id = :dishID LIMIT 1")
    LiveData<Integer> getDishQuantity(int dishID);

    @Query("SELECT * FROM cart_table WHERE dish_id = :dishID")
    LiveData<DishEntity> getDishCount(int dishID);

    /* TODO create return entity with quantity
    @Query("SELECT dish_id, count(*) FROM cart_table GROUP BY dish_id")
    LiveData<List<DishEntity>> getAllDishes();
    */

}
