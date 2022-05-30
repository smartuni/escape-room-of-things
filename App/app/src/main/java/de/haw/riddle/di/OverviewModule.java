package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;
import de.haw.riddle.ui.led.MainViewModel;
import de.haw.riddle.ui.overview.OverviewFragment;
import de.haw.riddle.ui.overview.OverviewViewModel;

@Module
public abstract class OverviewModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract OverviewFragment overviewFragment();

    @Binds
    @IntoMap
    @ViewModelKey(OverviewViewModel.class)
    abstract ViewModel bindViewModel(OverviewViewModel viewModel);
}
