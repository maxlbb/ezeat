package one.marcomass.ezeat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import one.marcomass.ezeat.db.entity.DishEntity;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;

public class MainActivity extends AppCompatActivity implements RestaurantFragment.RestaurantSelector {

    private MainViewModel mainVM;
    private SharedPreferences sharedPreferences;
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        sharedPreferences = getSharedPreferences(Util.PREFERENCES, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Util.TOKEN, null);
        updateLogin();

        RelativeLayout bottomSheet = findViewById(R.id.fragment_checkout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mainVM.getBottomSheetState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                bottomSheetBehavior.setState(state);
            }
        });

        //TODO bad way to disable dragging - review
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_DRAGGING
                        && mainVM.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (i == BottomSheetBehavior.STATE_DRAGGING
                        && mainVM.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        RestaurantFragment restaurantFragment = new RestaurantFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, restaurantFragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (token != null) {
            menu.findItem(R.id.action_account).setVisible(true);
            menu.findItem(R.id.action_login).setVisible(false);
        } else {
            menu.findItem(R.id.action_account).setVisible(false);
            menu.findItem(R.id.action_login).setVisible(true);
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_account:
                Intent intentLogout = new Intent(this, AccountActivity.class);
                startActivityForResult(intentLogout, Util.REQUEST_LOGOUT);
                break;
            case R.id.action_login:
                Intent intentLogin = new Intent(this, LoginActivity.class);
                startActivityForResult(intentLogin, Util.REQUEST_LOGIN);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case Util.REQUEST_LOGIN:
                if (resultCode == Activity.RESULT_OK) {
                    updateLogin();
                }
                break;
            case Util.REQUEST_LOGOUT:
                if (resultCode == Activity.RESULT_OK) {
                    updateLogin();
                }
        }
    }

    //TODO change params
    @Override
    public void selectRestaurant(Restaurant restaurant) {
        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Util.RESTAURANT_ID, restaurant.getID());
        menuFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.addToBackStack("fragment_menu");
        fragmentTransaction.commit();
    }

    private void updateLogin() {
        token = sharedPreferences.getString(Util.TOKEN, null);
        invalidateOptionsMenu();
        if (token != null) {
            mainVM.setIsLogged(true);
        }
    }
}
