 package de.haw.riddle.ui.admin.riddle;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.haw.riddle.net.admin.CreateRiddleDto;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import retrofit2.Call;

public class RiddleDetailViewModel extends ViewModel {

    public static final String TAG = RiddleDetailViewModel.class.getSimpleName();

    private final RiddleService riddleService;

    private Riddle riddle;
    private long id = -1;
    private String name = "";
    private String description="";
    private long parentRoomId;
    private String state="ready";


    @Inject
    public RiddleDetailViewModel(RiddleService riddleService) {
        this.riddleService = riddleService;
    }

    public void setData(Riddle riddle,long parentRoomId) {
        this.riddle = riddle;
        name = riddle.getName();
        id = riddle.getId();
        this.parentRoomId= parentRoomId;
        description=riddle.getDescription();
        state=riddle.getState();
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

    public String getDescription(){ return description;}

    public void setDescription(String description) {
        this.description = description;
    }

    public Call<Riddle> createRiddleCallIfValid() {
        List<Device> devices = riddle == null ? new ArrayList<>(0) : riddle.getDevices();
        final CreateRiddleDto riddle = new CreateRiddleDto(name,description,parentRoomId);
        if (riddle.isValid())
            return riddleService.createRiddle(riddle);
        else
            return null;
    }

    public Call<Riddle> deleteRiddle() {
        return riddleService.deleteRiddle(riddle.getId());
    }
}
