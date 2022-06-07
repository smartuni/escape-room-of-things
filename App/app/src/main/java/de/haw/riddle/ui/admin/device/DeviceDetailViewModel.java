 package de.haw.riddle.ui.admin.device;

import androidx.lifecycle.ViewModel;

import javax.inject.Inject;

import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.ui.admin.model.Device;

 public class DeviceDetailViewModel extends ViewModel {

     public static final String TAG = DeviceDetailViewModel.class.getSimpleName();

     private final DeviceService deviceService;

     private Device device;
     private String description="";
     private long devIP=-1;
     private long id=-1;
     private boolean isEventDevice=false;
     private String name="";
     private String nodeState="";
     private String publicKey="";
     private long puzzles=-1;
     private String serial="";
     private String state="";

     @Inject
     public DeviceDetailViewModel(DeviceService deviceService) {
         this.deviceService = deviceService;
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




 }
