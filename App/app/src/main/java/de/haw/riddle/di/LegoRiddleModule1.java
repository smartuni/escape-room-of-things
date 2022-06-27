package de.haw.riddle.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.haw.riddle.ui.lego.LegoRiddleFragmentPart1;

@Module
public abstract class LegoRiddleModule1 {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract LegoRiddleFragmentPart1 legoFragment();
}
