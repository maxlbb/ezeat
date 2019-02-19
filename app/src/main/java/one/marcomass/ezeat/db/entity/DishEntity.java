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
    private int dishID;

    @ColumnInfo(name = "quantity")
    private int quantity;

    @ColumnInfo(name = "price")
    private float price;

    @ColumnInfo(name = "name")
    private String name;

    public DishEntity(int id, int dishID, int quantity, float price, String name) {
        this.id = id;
        this.dishID = dishID;
        this.quantity = quantity;
        this.price = price;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public int getDishID() {
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
}
