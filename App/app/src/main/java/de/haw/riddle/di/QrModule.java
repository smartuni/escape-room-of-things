package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import de.haw.riddle.ui.led.MainViewModel;
import de.haw.riddle.ui.qr.QrFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class QrModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract QrFragment qrFragment();

//    @Binds
//    @IntoMap
//    @ViewModelKey(MainViewModel.class)
//    abstract ViewModel bindViewModel(MainViewModel viewModel);
}
