package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import de.haw.riddle.ui.led.LedFragment;
import de.haw.riddle.ui.led.MainViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class LedModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract LedFragment mainFragment();

//    @Binds
//    @IntoMap
//    @ViewModelKey(MainViewModel.class)
//    abstract ViewModel bindViewModel(MainViewModel viewModel);
}
