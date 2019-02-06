package one.marcomass.ezeat.db;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import one.marcomass.ezeat.db.entity.DishEntity;

@Database(entities = {DishEntity.class}, version = 1)
public abstract class CartRoomDatabase extends RoomDatabase {

    public abstract CartDAO cartDAO();

    private static volatile CartRoomDatabase INSTANCE;

    public static CartRoomDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CartRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            CartRoomDatabase.class, "cart_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
