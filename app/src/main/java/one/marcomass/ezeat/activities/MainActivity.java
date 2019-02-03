package one.marcomass.ezeat.activities;

import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;
import one.marcomass.ezeat.MainViewModel;
import one.marcomass.ezeat.R;
import one.marcomass.ezeat.SwitchPage;
import one.marcomass.ezeat.adapaters.ViewPagerAdapter;
import one.marcomass.ezeat.models.Restaurant;

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private View viewBack;

    private int screenWidth;
    private int oldPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                        break;
                    case 2:
                        //Non serve lo scale perchè non ho scheda successiva
                        viewBack.animate().scaleXBy(3.0f)
                                .setInterpolator(new AccelerateDecelerateInterpolator()).setDuration(200).start();
                        break;
                }
                oldPage = position;
            }
        });

        viewBack = findViewById(R.id.view_background);
        viewBack.getLayoutParams().width = screenWidth/3;

        //TEST VIEWMODEL
        MainViewModel mainVM = ViewModelProviders.of(this).get(MainViewModel.class);
        mainVM.getCurrentPage().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                viewPager.setCurrentItem(integer);
            }
        });
    }
}
