package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import de.haw.riddle.ui.led.LedRiddleFragment;
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
    abstract LedRiddleFragment ledFragment();


}
