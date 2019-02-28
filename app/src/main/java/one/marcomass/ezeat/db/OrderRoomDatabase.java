package one.marcomass.ezeat.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import one.marcomass.ezeat.db.entity.DishEntity;

@Database(entities = {DishEntity.class}, version = 1)
public abstract class OrderRoomDatabase extends RoomDatabase {

    public abstract OrderDAO orderDAO();

    private static volatile OrderRoomDatabase INSTANCE;

    public static OrderRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (OrderRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            OrderRoomDatabase.class, "order_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
