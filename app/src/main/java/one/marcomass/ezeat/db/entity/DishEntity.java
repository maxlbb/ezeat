package one.marcomass.ezeat.db.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart_table")
public class DishEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "dish_id")
    private int dishID;

    public DishEntity(int id, int dishID) {
        this.id = id;
        this.dishID = dishID;
    }

    public int getId() {
        return id;
    }

    public int getDishID() {
        return dishID;
    }
}
