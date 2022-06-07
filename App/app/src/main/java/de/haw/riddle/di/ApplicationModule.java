package de.haw.riddle.di;

import android.app.Application;
import android.content.Context;

import dagger.Binds;
import dagger.Module;
import de.haw.riddle.RiddleApplication;

@Module
public interface ApplicationModule {

    @Binds
    Application application(RiddleApplication application);

    @Binds
    Context context(RiddleApplication application);
}
