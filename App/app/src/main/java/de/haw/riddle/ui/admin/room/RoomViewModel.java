package de.haw.riddle.ui.admin.room;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Singleton;

import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.ui.admin.model.Config;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Resource;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class RoomViewModel extends ViewModel {

    private static final String TAG = RoomViewModel.class.getSimpleName();

    private final MutableLiveData<List<Room>> rooms = new MutableLiveData<>(new ArrayList<>(0));
    private final RoomService roomService;

    @Inject
    public RoomViewModel(RoomService roomService) {
        this.roomService = roomService;
        Config dummyData = createDummyData();
        rooms.setValue(dummyData.getRooms());
    }

    public void sync(@Nullable SwipeRefreshLayout swipeRefreshLayout) {
        roomService.getRooms().enqueue(new Callback<List<Room>>() {
            @Override
            public void onResponse(@NonNull Call<List<Room>> call, @NonNull Response<List<Room>> response) {
                rooms.setValue(response.body());
                if (swipeRefreshLayout != null)
                    swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Room>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to get rooms from api", t);
                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(swipeRefreshLayout.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
                }
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

    private Config createDummyData() {
        List<Resource> dummyResource = makeResourceList();
        Map<String, List<Resource>> dummyDeviceList = makeDeviceList(dummyResource);
        List<Riddle> dummyRiddleList = makePuzzleList(dummyDeviceList);
        Room room1 = new Room(1, "RoomDummy1", "description1", "ready", dummyRiddleList);
        Room room2 = new Room(2, "RoomDummy2", "description2", "ready", dummyRiddleList);
        Room room3 = new Room(3, "RoomDummy3", "description3", "ready", dummyRiddleList);
        Room room4 = new Room(4, "RoomDummy4", "description4", "ready", dummyRiddleList);

        List<Room> dummyRoomList = new ArrayList<>();
        dummyRoomList.add(room1);
        dummyRoomList.add(room2);
        dummyRoomList.add(room3);
        dummyRoomList.add(room4);

        return new Config(dummyRoomList);
    }


    private List<Resource> makeResourceList() {
        Resource resource1 = new Resource("LED1", "1");
        Resource resource2 = new Resource("LED2", "2");
        Resource resource3 = new Resource("LED3", "3");
        Resource resource4 = new Resource("LED4", "4");
        Resource resource5 = new Resource("LED5", "5");

        List<Resource> resourceList = new ArrayList<>();
        resourceList.add(resource1);
        resourceList.add(resource2);
        resourceList.add(resource3);
        resourceList.add(resource4);
        resourceList.add(resource5);

        return resourceList;
    }

    private Map<String, List<Resource>> makeDeviceList(List<Resource> resourceList) {
        Map<String, List<Resource>> deviceList = new HashMap<>();
        deviceList.put("Device", resourceList);

        return deviceList;
    }

    private List<Riddle> makePuzzleList(Map<String, List<Resource>> deviceList) {
        ArrayList <String> resource1= new ArrayList<>();
        resource1.add("resource1");

        Device device1 = new Device(1,resource1,"Device1");
        Device device2 = new Device(2, resource1,"Device2");
        Device device3 = new Device(3, resource1,"Device3");
        Device device4 = new Device(4,resource1,"Device4");

        List<Device> deviceList1 = new ArrayList<>();
        deviceList1.add(device1);
        deviceList1.add(device2);
        deviceList1.add(device3);
        deviceList1.add(device4);

        Riddle puzzle1 = new Riddle(1,"1",deviceList1);
        Riddle puzzle2 = new Riddle(2,"2",deviceList1);
        Riddle puzzle3 = new Riddle(3,"3",deviceList1);
        Riddle puzzle4 = new Riddle(4,"4",deviceList1);

        List<Riddle> puzzleList = new ArrayList<>();
        puzzleList.add(puzzle1);
        puzzleList.add(puzzle2);
        puzzleList.add(puzzle3);
        puzzleList.add(puzzle4);

        return puzzleList;
    }
}
