package one.marcomass.ezeat.activities;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import one.marcomass.ezeat.viewmodels.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.Util;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.fragments.RestaurantFragment;
import one.marcomass.ezeat.models.Restaurant;

public class MainActivity extends AppCompatActivity implements RestaurantFragment.RestaurantSelector {

    private MainViewModel mainVM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);

        RelativeLayout bottomSheet = findViewById(R.id.fragment_checkout);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);

        mainVM.getBottomSheetOpen().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean open) {
                if (open) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });

        //TODO bad way to disable dragging
        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                if (i == BottomSheetBehavior.STATE_DRAGGING && mainVM.getBottomSheetOpen().getValue()) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else if (i == BottomSheetBehavior.STATE_DRAGGING && !mainVM.getBottomSheetOpen().getValue()){
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

    //TODO change params
    @Override
    public void selectRestaurant(Restaurant restaurant) {
        MenuFragment menuFragment = new MenuFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Util.RESTAURANT_ID, restaurant.getID());
        menuFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, menuFragment);
        fragmentTransaction.addToBackStack("fragment_menu");
        fragmentTransaction.commit();
    }
}
