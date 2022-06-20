package de.haw.riddle.di;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import de.haw.riddle.ui.lego.LegoRiddleFragmentPart2;

@Module
public abstract class LegoRiddleModule2 {

    @ContributesAndroidInjector(modules = {
            ViewModelBuilder.class
    })
    abstract LegoRiddleFragmentPart2 legoFragment();
}
