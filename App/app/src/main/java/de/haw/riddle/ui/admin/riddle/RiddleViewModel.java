package de.haw.riddle.ui.admin.riddle;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.haw.riddle.net.admin.GetRiddleResponse;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.ui.admin.device.DeviceDetailViewModel;
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
    private Room parentRoom;
    private RoomService roomService;

    @Inject
    public RiddleViewModel(RiddleService riddleService, RoomService roomService) {
        this.riddleService = riddleService;
        this.roomService= roomService;
    }

    public void sync(SwipeRefreshLayout swipeRefreshLayout) {
        roomService.getRoomById(parentRoom.getId()).enqueue(new Callback<Room>() {
            @Override
            public void onResponse(Call<Room> call, Response<Room> response) {
                swipeRefreshLayout.setRefreshing(false);

                if(response.isSuccessful())
                {
                    System.out.println(response.body());
                    riddles.setValue(response.body().getRiddles());

                }
                else
                {
                    try {
                        final String errorBody = response.errorBody().string();
                        Log.e(TAG,errorBody);
                        Toast.makeText(swipeRefreshLayout.getContext(),errorBody,Toast.LENGTH_SHORT).show();
                        // TODO getContext via Application

                    } catch (IOException e) {
                        Log.wtf(TAG, "Failed to parse errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Room> call, Throwable t) {

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(swipeRefreshLayout.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });


        //TODO load new data from api
    }

    public void setRoom(Room room) {
        this.parentRoom=room;
        this.riddles.setValue(room.getRiddles());
    }

    public void addRiddle(Riddle riddle) {
        List<Riddle> riddles = Objects.requireNonNull(this.riddles.getValue());
        riddles.add(riddle);
        this.riddles.setValue(riddles);
    }

    public LiveData<List<Riddle>> getRiddle() {
        return riddles;
    }


    public boolean removeRiddle(Riddle riddle) {
        List<Riddle> riddles = Objects.requireNonNull(this.riddles.getValue());
        final boolean isRemoved = riddles.remove(riddle);
        this.riddles.setValue(riddles);
        return isRemoved;
    }

    public Room getParentRoom() {
        return parentRoom;
    }
}