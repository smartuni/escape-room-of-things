 package de.haw.riddle.ui.admin.device;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.model.UpdateDeviceDto;
import de.haw.riddle.ui.admin.room.RoomViewModel;
import retrofit2.Call;

 public class DeviceDetailViewModel extends ViewModel {

     public static final String TAG = DeviceDetailViewModel.class.getSimpleName();

     private final DeviceService deviceService;
     private final RoomViewModel roomViewModel;

     private Device device;
     private String description="";
     private String devIP="";
     private long id=-1;
     private boolean isEventDevice=false;
     private String name="";
     private String nodeState="";
     private String publicKey="";
     private long puzzles=-1;
     private String serial="";
     private String state="";
     private Riddle riddleParent;

     @Inject
     public DeviceDetailViewModel(DeviceService deviceService, RoomViewModel roomViewModel) {
         this.deviceService = deviceService;
         this.roomViewModel = roomViewModel;
     }

     public void setDevice(Device device) {
         this.device = device;
         description= device.getDescription();
         devIP= device.getDevIP();
         id= device.getId();
         isEventDevice=device.isEventDevice();
         name=device.getName();
         nodeState=device.getNodeState();
         publicKey=device.getPublicKey();
         puzzles=device.getParentPuzzleId();
         serial=device.getSerial();
         state=device.getState();
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

     public  Riddle[] getPuzzleList(){
         final List<Room> rooms = roomViewModel.getRooms().getValue();



         List<Riddle> puzzleList= new ArrayList<>();

         for (Room room : rooms) {
             puzzleList.addAll(room.getRiddles());

         }

         return  puzzleList.toArray(new Riddle[0]);
     }


     public void setParentPuzzle(Riddle item) {
         riddleParent=item;
     }

     public Call<Device> updateDevice(){
         return deviceService.updateDevice(id,new UpdateDeviceDto(riddleParent.getId(),isEventDevice));

     }
 }
