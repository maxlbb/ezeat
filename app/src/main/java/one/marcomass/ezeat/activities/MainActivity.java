package one.marcomass.ezeat.activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
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

        /*
        bottomSheetLayout = findViewById(R.id.bottom_sheet_checkout);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);

        bottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {

            }

            @Override
            public void onSlide(@NonNull View view, float v) {

            }
        });
        */

        RestaurantFragment restaurantFragment = new RestaurantFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, restaurantFragment);
        fragmentTransaction.commit();

        /*
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        screenWidth = size.x;

        viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager()));
        viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                float scale = 0;
                switch (position) {
                    case 0:
                        scale = (oldPage > 0 ? -2.0f : 1.0f);
                        viewBack.animate().scaleXBy(scale)
                                .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200).start();
                        break;
                    case 1:
                        scale = (oldPage > 1 ? -3.0f : 2.0f);
                        viewBack.animate().scaleXBy(scale)
                                .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200).start();
                        invalidateOptionsMenu();
                        break;
                    case 2:
                        //Non serve lo scale perchè non ho scheda successiva
                        viewBack.animate().scaleXBy(3.0f)
                                .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200).start();
                        //TODO find a better way to do this
                        whiteMenu = true;
                        invalidateOptionsMenu();
                        break;
                }
                oldPage = position;
            }
        });

        viewBack = findViewById(R.id.view_background);
        viewBack.getLayoutParams().width = screenWidth / 3;

        //TEST VIEWMODEL
        mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        mainVM.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewPager.setCurrentItem(integer);
            }
        });
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        mainVM.removeAllFromCart();

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem itemRed = menu.findItem(R.id.action_cart);
        MenuItem itemWhite = menu.findItem(R.id.action_cart_white);

        /*
        if(whiteMenu) {
            itemRed.setVisible(false);
            itemWhite.setVisible(true);
            Log.d("page", "try");
            whiteMenu = false;
        } else {
            itemRed.setVisible(true);
            itemWhite.setVisible(false);
        }
        */

        return super.onPrepareOptionsMenu(menu);
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
