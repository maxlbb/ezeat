package one.marcomass.ezeat.repos;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import java.util.List;

import androidx.lifecycle.LiveData;
import one.marcomass.ezeat.db.OrderDAO;
import one.marcomass.ezeat.db.OrderRoomDatabase;
import one.marcomass.ezeat.db.entity.DishEntity;

public class OrderRepository {

    private static OrderRepository INSTANCE = null;

    private OrderDAO orderDAO;

    private OrderRepository(Application application) {
        OrderRoomDatabase orderRoomDatabase = OrderRoomDatabase.getDatabase(application);
        orderDAO = orderRoomDatabase.orderDAO();
    }

    public static OrderRepository getInstance(Application application) {
        if (INSTANCE == null) {
            return new OrderRepository(application);
        }
        return INSTANCE;
    }

    public LiveData<Integer> getDishQuantity(String dishID) {
        return orderDAO.getDishQuantity(dishID);
    }

    public LiveData<List<DishEntity>> getAllDishes() {
        return orderDAO.getAllDishes();
    }

    public LiveData<Float> getTotal() {
        return orderDAO.getTotal();
    }

    public void add(DishEntity dish) {
        new insertAsyncTask(orderDAO).execute(dish);
    }

    public void removeAll() {
        new removeAllAsyncTask(orderDAO).execute();
    }

    public void removeDish(String dishID) {
        new removeDishAsyncTask(orderDAO).execute(dishID);
    }

    public void removeAllDish(String dishID) { new removeAllDishAsyncTask(orderDAO).execute(dishID); }

    private static class insertAsyncTask extends AsyncTask<DishEntity, Void, Boolean> {

        private OrderDAO mAsyncTaskDao;

        insertAsyncTask(OrderDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Boolean doInBackground(final DishEntity... params) {
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

        private OrderDAO mAsyncTaskDao;

        removeAllAsyncTask(OrderDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            mAsyncTaskDao.removeAll();
            return null;
        }
    }

    private static class removeDishAsyncTask extends AsyncTask<String, Void, Void> {

        private OrderDAO mAsyncTaskDao;

        removeDishAsyncTask(OrderDAO dao) {
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

        private OrderDAO mAsyncTaskDao;

        removeAllDishAsyncTask(OrderDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final String... params) {
            mAsyncTaskDao.removeAllDish(params[0]);
            return null;
        }
    }
}
