package one.marcomass.ezeat.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;
import one.marcomass.ezeat.viewmodels.MainViewModel;

public class MainActivity extends AppCompatActivity implements RestaurantFragment.RestaurantSelector {

    private MainViewModel mainVM;
    private SharedPreferences sharedPreferences;
    private LinearLayout linearCheckout;
    private ImageButton buttonClose;
    private String token;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        linearCheckout = findViewById(R.id.linear_checkout);
        buttonClose = findViewById(R.id.button_checkout_close);

        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        sharedPreferences = getSharedPreferences(Util.PREFERENCES, Context.MODE_PRIVATE);
        token = sharedPreferences.getString(Util.TOKEN, null);
        updateLogin();

        final LinearLayout bottomSheet = findViewById(R.id.fragment_checkout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mainVM.getBottomSheetState().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer state) {
                if (state == BottomSheetBehavior.STATE_COLLAPSED) {
                    linearCheckout.animate().translationY(-linearCheckout.getHeight()).setDuration(200).start();
                } else if (state == BottomSheetBehavior.STATE_EXPANDED) {
                    linearCheckout.animate().translationY(linearCheckout.getHeight()).setDuration(200).start();
                }
                bottomSheetBehavior.setState(state);
            }
        });

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int state) {
                switch (state) {
                    case BottomSheetBehavior.STATE_COLLAPSED:
                        mainVM.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        mainVM.setBottomSheetState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                }

                //TODO bad try to disable dragging
                if (state == BottomSheetBehavior.STATE_DRAGGING
                        && mainVM.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (state == BottomSheetBehavior.STATE_DRAGGING
                        && mainVM.getBottomSheetState().getValue() == BottomSheetBehavior.STATE_COLLAPSED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });

        buttonClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainVM.setBottomSheetState(BottomSheetBehavior.STATE_COLLAPSED);
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
    public void selectRestaurant(final String restaurantID) {
        //TODO temp - check order restaurant id
        //TODO find a better way to check if order exist
        String oldRestaurantID = sharedPreferences.getString(Util.RESTAURANT_ID, null);
        if (restaurantID.equals(oldRestaurantID) || oldRestaurantID == null) {
            openRestaurantFragment(restaurantID);
        } else {
            new AlertDialog.Builder(this, R.style.ThemeOverlay_MaterialComponents_Dialog_Alert)
                    .setTitle("Hai già un ordine")
                    .setMessage("Puoi ordinare da un unico ristorante contemporaneamente. \nVuoi eliminare l'ordine precedente?")
                    .setPositiveButton("Elimina", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            openRestaurantFragment(restaurantID);
                            mainVM.removeAllFromCart();
                        }
                    })
                    .setNegativeButton("Annulla", null)
                    .show();
        }
    }

    private void updateLogin() {
        token = sharedPreferences.getString(Util.TOKEN, null);
        invalidateOptionsMenu();
        if (token != null) {
            mainVM.setIsLogged(true);
        }
    }

    private void openRestaurantFragment(String restaurantID) {
        sharedPreferences.edit().putString(Util.RESTAURANT_ID, restaurantID).apply();
        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putString(Util.RESTAURANT_ID, restaurantID);
        menuFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.addToBackStack("fragment_menu");
        fragmentTransaction.commit();
    }
}
