package one.marcomass.ezeat.adapaters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import one.marcomass.ezeat.fragments.CartFragment;
import one.marcomass.ezeat.fragments.MenuFragment;
import one.marcomass.ezeat.fragments.RestaurantFragment;

public class ViewPagerAdapter extends FragmentPagerAdapter {

    public ViewPagerAdapter(FragmentManager fragmentManager) {
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new RestaurantFragment();
            case 1:
                return new MenuFragment();
            case 2:
                return new CartFragment();
            default:
                return null;

        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
