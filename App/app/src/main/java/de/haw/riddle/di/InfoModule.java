package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import de.haw.riddle.ui.info.InfoFragment;
import de.haw.riddle.ui.info.InfoViewModel;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class InfoModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract InfoFragment infoFragment();

    @Binds
    @IntoMap
    @ViewModelKey(InfoViewModel.class)
    abstract ViewModel bindViewModel(InfoViewModel viewModel);
}
