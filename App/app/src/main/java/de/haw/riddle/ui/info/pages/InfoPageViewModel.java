package de.haw.riddle.ui.info.pages;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import java.util.Locale;

public class InfoPageViewModel extends ViewModel {

    private final MutableLiveData<Integer> index = new MutableLiveData<>();
    private final LiveData<String> text = Transformations.map(index, input -> String.format(Locale.GERMAN, "InfoPageFragment number %d", input));

    public void setIndex(int index) {
        this.index.setValue(index);
    }

    public LiveData<String> getText() {
        return text;
    }
}