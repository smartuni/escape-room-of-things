package de.haw.riddle.ui.admin.riddle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class RiddleViewModel extends ViewModel {

    private static final String TAG = RiddleViewModel.class.getSimpleName();

    private final MutableLiveData<List<Riddle>> riddles = new MutableLiveData<>(new ArrayList<>(0));
    private final RiddleService riddleService;

    @Inject
    public RiddleViewModel(RiddleService riddleService) {
        this.riddleService = riddleService;
    }

    public void sync(SwipeRefreshLayout swipeRefreshLayout) {
        riddleService.getRiddles().enqueue(new Callback<List<Riddle>>() {
            @Override
            public void onResponse(@NonNull Call<List<Riddle>> call, @NonNull Response<List<Riddle>> response) {
                riddles.setValue(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Riddle>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to get rooms from api", t);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(swipeRefreshLayout.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //TODO load new data from api
    }

    public void setRoom(Room room) {
        List<Riddle> riddles = this.riddles.getValue();
        assert riddles != null;
        riddles.clear();
        riddles.addAll(room.getRiddles());
        this.riddles.setValue(riddles);
    }

    public void addRiddle(Riddle riddle) {
        List<Riddle> riddles = Objects.requireNonNull(this.riddles.getValue());
        riddles.add(riddle);
        this.riddles.setValue(riddles);
    }

    public LiveData<List<Riddle>> getRiddle() {
        return riddles;
    }


}