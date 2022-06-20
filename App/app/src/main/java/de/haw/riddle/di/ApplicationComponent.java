package de.haw.riddle.di;

import android.content.Context;

import de.haw.riddle.RiddleApplication;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;

@Singleton
@Component(
        modules = {
                AndroidSupportInjectionModule.class,
                OverviewModule.class,
                WaterRiddleModule.class,
                LegoRiddleModule1.class,
                LegoRiddleModule2.class,
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
        ApplicationComponent create(@BindsInstance Context applicationContext);
    }
}