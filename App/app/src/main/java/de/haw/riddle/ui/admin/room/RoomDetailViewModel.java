package de.haw.riddle.ui.admin.room;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.haw.riddle.net.admin.CreateRoomDto;
import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import retrofit2.Call;

public class RoomDetailViewModel extends ViewModel {

    public static final String TAG = RoomDetailViewModel.class.getSimpleName();

    private final RoomService roomService;

    private Room room;
    private long id = -1;
    private String name = "";
    private String description = "";
    private String state = "Ready";

    @Inject
    public RoomDetailViewModel(RoomService roomService) {
        this.roomService = roomService;
    }

    public void setRoom(Room room) {
        this.room = room;
        name = room.getName();
        id = room.getId();
    }

    public long getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getState() {
        return state;
    }

    public Call<Room> createRoomCallIfValid() {
        List<Riddle> puzzles = room == null ? new ArrayList<>(0) : room.getRiddles();
        final CreateRoomDto room = new CreateRoomDto(name,description);
        if (room.isValid())
            return roomService.createRoom(room);
        else
            return null;
    }

    public Call<Room> deleteRoom() {
        return roomService.deleteRoom(room.getId());
    }
}
