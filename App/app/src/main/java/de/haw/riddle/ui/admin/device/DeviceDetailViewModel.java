 package de.haw.riddle.ui.admin.device;

import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Resource;
import de.haw.riddle.ui.admin.model.Riddle;
import retrofit2.Call;

 public class DeviceDetailViewModel extends ViewModel {

     public static final String TAG = DeviceDetailViewModel.class.getSimpleName();

     private final DeviceService deviceService;

     private Device device;
     private long id = -1;
     private String name = "";

     @Inject
     public DeviceDetailViewModel(DeviceService deviceService) {
         this.deviceService = deviceService;
     }

     public void setDevice(Device device) {
         this.device = device;
         name = device.getName();
         id = device.getId();
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

     public Call<Device> createDeviceCallIfValid() {
         List<String> devices = device == null ? new ArrayList<>(0) : device.getResources();
         final Device device1 = new Device(id,devices,name);
         if (device.isValid())
             return deviceService.createDevice(this.device);
         else
             return null;
     }

 }
