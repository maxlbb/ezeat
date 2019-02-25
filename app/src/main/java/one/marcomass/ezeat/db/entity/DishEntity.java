package one.marcomass.ezeat.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table", indices=@Index(value = "dish_id", unique = true))
public class DishEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "dish_id")
    private String dishID;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "restaurant_id")
    private String restaurantID;

    public DishEntity(int id, String dishID, int quantity, float price, String name, String restaurantID) {
        this.id = id;
        this.dishID = dishID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
        this.restaurantID = restaurantID;
    }

    public int getId() {
        return id;
    }

    public String getDishID() {
        return dishID;
    }

    public int getQuantity() {
        return quantity;
    }

    public float getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public String getRestaurantID() {
        return restaurantID;
    }
}
