package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;
import de.haw.riddle.ui.led.MainViewModel;
import de.haw.riddle.ui.lego.LegoRiddleFragment;
import de.haw.riddle.ui.overview.OverviewViewModel;
import de.haw.riddle.ui.water.WaterRiddleFragment;

@Module
public abstract class LegoRiddleModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract LegoRiddleFragment legoFragment();
}
