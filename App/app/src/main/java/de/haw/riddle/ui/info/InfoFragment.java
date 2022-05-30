package de.haw.riddle.ui.info;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager2.widget.ViewPager2;

import de.haw.riddle.R;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import dagger.android.support.DaggerFragment;

public class InfoFragment extends DaggerFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ViewPager2 pager = view.findViewById(R.id.viewPager);
        final TabLayout tabLayout = view.findViewById(R.id.tabLayout);
        pager.setAdapter(new ScreenSliderPagerAdapter(this));
        final TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, pager, (tab, position) -> {

        });
        tabLayoutMediator.attach();
    }

    @Override
    public void onStart() {
        super.onStart();
        //((MainActivity) requireActivity()).hideDrawerAndMenu();
    }
}
