package one.marcomass.ezeat.db;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import one.marcomass.ezeat.db.entity.DishEntity;

@Dao
public interface CartDAO {

    @Insert
    void insertDish(DishEntity dish);

    @Query("DELETE FROM cart_table WHERE dish_id LIKE :dishID")
    void removeDish(int dishID);

    @Query("DELETE FROM cart_table")
    void removeAll();

    @Query("SELECT * FROM cart_table")
    LiveData<List<DishEntity>> getAllDishes();

    @Query("SELECT count(*) FROM cart_table WHERE dish_id = :dishID")
    LiveData<Integer> getDishCount(int dishID);

    /* TODO create return entity with quantity
    @Query("SELECT dish_id, count(*) FROM cart_table GROUP BY dish_id")
    LiveData<List<DishEntity>> getAllDishes();
    */

}
