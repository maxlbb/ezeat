package one.marcomass.ezeat.repos;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import one.marcomass.ezeat.db.CartDAO;
import one.marcomass.ezeat.db.CartRoomDatabase;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.models.Dish;

public class CartRepository {

    private CartDAO cartDAO;

    public CartRepository(Application application) {
        CartRoomDatabase cartRoomDatabase = CartRoomDatabase.getDatabase(application);
        cartDAO = cartRoomDatabase.cartDAO();
    }

    public LiveData<Integer> getDishQuantity(String dishID) {
        return cartDAO.getDishQuantity(dishID);
    }

    public LiveData<List<DishEntity>> getAllDishes() {
        return cartDAO.getAllDishes();
    }

    public LiveData<Float> getTotal() {
        return cartDAO.getTotal();
    }

    public void add(DishEntity dish) {
        new insertAsyncTask(cartDAO).execute(dish);
    }

    public void removeAll() {
        new removeAllAsyncTask(cartDAO).execute();
    }

    public void removeDish(String dishID) {
        new removeDishAsyncTask(cartDAO).execute(dishID);
    }

    public void removeAllDish(String dishID) { new removeAllDishAsyncTask(cartDAO).execute(dishID); }

    private static class insertAsyncTask extends AsyncTask<DishEntity, Void, Void> {

        private CartDAO mAsyncTaskDao;

        insertAsyncTask(CartDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DishEntity... params) {
            DishEntity dish = mAsyncTaskDao.getDishByID(params[0].getDishID());
            if (dish != null) {
                mAsyncTaskDao.addDish(dish.getDishID());
            } else {
                mAsyncTaskDao.insertDish(params[0]);
            }
            return null;
        }
    }

    private static class removeAllAsyncTask extends AsyncTask<Void, Void, Void> {

        private CartDAO mAsyncTaskDao;

        removeAllAsyncTask(CartDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.removeAll();
            return null;
        }
    }

    private static class removeDishAsyncTask extends AsyncTask<String, Void, Void> {

        private CartDAO mAsyncTaskDao;

        removeDishAsyncTask(CartDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            DishEntity dish = mAsyncTaskDao.getDishByID(params[0]);
            if (dish != null) {
                if (dish.getQuantity() > 1) {
                    mAsyncTaskDao.removeDish(params[0]);
                } else {
                    Log.d("data", "removing " + params[0]);
                    mAsyncTaskDao.removeAllDish(params[0]);
                }
            }
            return null;
        }
    }

    private static class removeAllDishAsyncTask extends AsyncTask<String, Void, Void> {

        private CartDAO mAsyncTaskDao;

        removeAllDishAsyncTask(CartDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.removeAllDish(params[0]);
            return null;
        }
    }
}
