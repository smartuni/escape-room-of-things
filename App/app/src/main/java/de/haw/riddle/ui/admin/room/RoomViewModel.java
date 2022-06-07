package de.haw.riddle.ui.admin.room;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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

import de.haw.riddle.net.ApiResponse;
import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.ui.admin.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class RoomViewModel extends ViewModel {

    private static final String TAG = RoomViewModel.class.getSimpleName();

    private final MutableLiveData<List<Room>> rooms = new MutableLiveData<>(new ArrayList<>(0));
    private final RoomService roomService;
    private final Application application;

    @Inject
    public RoomViewModel(RoomService roomService, Application application) {
        this.roomService = roomService;
        //Config dummyData = createDummyData();
        //rooms.setValue(dummyData.getRooms());
        this.application = application;
    }

    public void sync(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        roomService.getRooms().enqueue(new Callback<ApiResponse<List<Room>>>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse<List<Room>>> call, @NonNull Response<ApiResponse<List<Room>>> response) {
                if (response.isSuccessful()) {
                    rooms.setValue(response.body().getData());
                } else {
                    try {
                        final String errorBody = response.errorBody().string();
                        Log.e(TAG, errorBody);
                        Toast.makeText(application.getBaseContext(), errorBody, Toast.LENGTH_SHORT).show();

                    } catch (IOException e) {
                        Log.wtf(TAG, "Failed to parse errorBody", e);
                    }
                }
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse<List<Room>>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to get rooms from api", t);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(application.getBaseContext(), t.toString(), Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void addRoom(Room room) {
        List<Room> rooms = Objects.requireNonNull(this.rooms.getValue());
        rooms.add(room);
        this.rooms.setValue(rooms);
    }

    public boolean removeRoom(Room room) {
        List<Room> rooms = Objects.requireNonNull(this.rooms.getValue());
        final boolean isRemoved = rooms.remove(room);
        this.rooms.setValue(rooms);
        return isRemoved;
    }

    public LiveData<List<Room>> getRooms() {
        return rooms;
    }

}
