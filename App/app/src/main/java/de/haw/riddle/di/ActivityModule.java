package de.haw.riddle.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.haw.riddle.MainActivity;

@Module
public abstract class ActivityModule {
    @ContributesAndroidInjector
    abstract MainActivity provideMainActivity();
}
