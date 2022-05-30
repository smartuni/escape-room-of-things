package de.haw.riddle.ui.admin.device;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

import de.haw.riddle.net.admin.DeviceService;
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
public class DeviceViewModel extends ViewModel {

    private static final String TAG = DeviceViewModel.class.getSimpleName();

    private final MutableLiveData<List<Device>> device = new MutableLiveData<>(new ArrayList<>(0));
    private final DeviceService deviceService;

    @Inject
    public DeviceViewModel(DeviceService deviceService) {
        this.deviceService = deviceService;
    }

    public void sync(SwipeRefreshLayout swipeRefreshLayout) {
        deviceService.getDevice().enqueue(new Callback<List<Device>>() {
            @Override
            public void onResponse(@NonNull Call<List<Device>> call, @NonNull Response<List<Device>> response) {
                device.setValue(response.body());
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onFailure(@NonNull Call<List<Device>> call, @NonNull Throwable t) {
                Log.e(TAG, "Failed to get rooms from api", t);
                swipeRefreshLayout.setRefreshing(false);
                Toast.makeText(swipeRefreshLayout.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        //TODO load new data from api
    }

    public void addDevice(Device device) {
        List<Device> devices = Objects.requireNonNull(this.device.getValue());
        devices.add(device);
        this.device.setValue(devices);
    }

    public LiveData<List<Device>> getDevice() {
        return device;
    }


}