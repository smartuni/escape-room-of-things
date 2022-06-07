package de.haw.riddle.di;

import de.haw.riddle.ui.admin.device.QrFragment;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;

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
