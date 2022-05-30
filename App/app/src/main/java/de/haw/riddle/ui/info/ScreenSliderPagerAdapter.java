package de.haw.riddle.ui.info;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import de.haw.riddle.ui.info.pages.InfoPageFragment;

import java.util.ArrayList;
import java.util.List;

public class ScreenSliderPagerAdapter extends FragmentStateAdapter {

    private final List<Fragment> pagerFragment = new ArrayList<>(0);

    public ScreenSliderPagerAdapter(@NonNull Fragment fragment) {
        super(fragment);
        pagerFragment.add(InfoPageFragment.newInstance(0));
        pagerFragment.add(InfoPageFragment.newInstance(1));
        pagerFragment.add(InfoPageFragment.newInstance(2));
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        return pagerFragment.get(position);
    }

    @Override
    public int getItemCount() {
        return pagerFragment.size();
    }
}
