package de.haw.riddle.di;

import androidx.lifecycle.ViewModel;

import de.haw.riddle.ui.admin.device.DeviceDetailFragment;
import de.haw.riddle.ui.admin.device.DeviceDetailViewModel;
import de.haw.riddle.ui.admin.device.DeviceFragment;
import de.haw.riddle.ui.admin.device.DeviceViewModel;
import de.haw.riddle.ui.admin.riddle.RiddleDetailFragment;
import de.haw.riddle.ui.admin.riddle.RiddleDetailViewModel;
import de.haw.riddle.ui.admin.riddle.RiddleViewModel;
import de.haw.riddle.ui.admin.room.RoomDetailFragment;
import de.haw.riddle.ui.admin.room.RoomDetailViewModel;
import de.haw.riddle.ui.admin.room.RoomViewModel;
import de.haw.riddle.ui.admin.settings.AdminSettingsFragment;
import de.haw.riddle.ui.admin.riddle.RiddleFragment;
import de.haw.riddle.ui.admin.room.RoomFragment;

import dagger.Binds;
import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import dagger.multibindings.IntoMap;

@Module
public abstract class AdminModule {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract RoomFragment roomFragment();

    @Binds
    @IntoMap
    @ViewModelKey(RoomViewModel.class)
    abstract ViewModel bindRoomViewModel(RoomViewModel viewModel);

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract RoomDetailFragment roomDetailFragment();

    @Binds
    @IntoMap
    @ViewModelKey(RoomDetailViewModel.class)
    abstract ViewModel bindRoomDetailViewModel(RoomDetailViewModel viewModel);

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract RiddleFragment riddleFragment();

    @Binds
    @IntoMap
    @ViewModelKey(RiddleViewModel.class)
    abstract ViewModel bindRiddleViewModel(RiddleViewModel viewModel);

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract RiddleDetailFragment riddleDetailFragment();



    @Binds
    @IntoMap
    @ViewModelKey(RiddleDetailViewModel.class)
    abstract ViewModel bindRiddleDetailViewModel(RiddleDetailViewModel viewModel);


    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract DeviceFragment deviceFragment();

    @Binds
    @IntoMap
    @ViewModelKey(DeviceViewModel.class)
    abstract ViewModel bindDeviceViewModel(DeviceViewModel viewModel);


    @Binds
    @IntoMap
    @ViewModelKey(DeviceDetailViewModel.class)
    abstract ViewModel bindDeviceDetailViewModel(DeviceDetailViewModel viewModel);

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract DeviceDetailFragment deviceDetailFragment();



    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract AdminSettingsFragment adminSettingsFragment();
}
