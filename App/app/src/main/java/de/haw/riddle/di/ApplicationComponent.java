package de.haw.riddle.di;

import android.content.Context;

import dagger.Provides;
import dagger.android.AndroidInjectionModule;
import de.haw.riddle.RiddleApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import de.haw.riddle.ui.riddle1.Riddle1Fragment;

@Singleton
@Component(
        modules = {
                AndroidInjectionModule.class,
                ApplicationModule.class,
                OverviewModule.class,
                Riddle1Module.class,
                NetworkModule.class,
                QrModule.class,
                LedModule.class,
                AdminModule.class,
                InfoModule.class,
                ViewModelBuilder.class
        })
public interface ApplicationComponent extends AndroidInjector<RiddleApplication> {

    @Component.Factory
    interface Factory {
        ApplicationComponent create(@BindsInstance RiddleApplication application);
    }
}