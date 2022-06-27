package de.haw.riddle.ui.admin.device;

import android.util.Log;
import android.widget.Toast;

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

import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@Singleton
public class DeviceViewModel extends ViewModel {

    private static final String TAG = DeviceViewModel.class.getSimpleName();

    private final MutableLiveData<List<Device>> devices = new MutableLiveData<>(new ArrayList<>(0));
    private final DeviceService deviceService;
    private final RiddleService riddleService;
    private Riddle parentRiddle;

    @Inject
    public DeviceViewModel(DeviceService deviceService, RiddleService riddleService) {
        this.deviceService = deviceService;
        this.riddleService = riddleService;
    }

    public void sync(SwipeRefreshLayout swipeRefreshLayout) {
        riddleService.getRiddles(parentRiddle.getId()).enqueue(new Callback<Riddle>() {
            @Override
            public void onResponse(Call<Riddle> call, Response<Riddle> response) {
                Log.i(TAG, "ResponseCode= "+response.code());
                swipeRefreshLayout.setRefreshing(false);

                if (response.isSuccessful()) {
                    if (response.body().getDevices() != null)
                        devices.setValue(response.body().getDevices());

                } else {
                    try {
                        final String errorBody = response.errorBody().string();
                        Log.e(TAG, errorBody);
                        Toast.makeText(swipeRefreshLayout.getContext(), errorBody, Toast.LENGTH_SHORT).show();
                        // TODO getContext via Application

                    } catch (IOException e) {
                        Log.wtf(TAG, "Failed to parse errorBody", e);
                    }
                }
            }

            @Override
            public void onFailure(Call<Riddle> call, Throwable t) {

                if (swipeRefreshLayout != null) {
                    swipeRefreshLayout.setRefreshing(false);
                }
                Toast.makeText(swipeRefreshLayout.getContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addDevice(Device device) {
        List<Device> devices = Objects.requireNonNull(this.devices.getValue());
        devices.add(device);
        this.devices.setValue(devices);
    }

    public LiveData<List<Device>> getDevice() {
        return devices;
    }


    public void setRiddle(Riddle riddle) {

        this.parentRiddle = riddle;
        devices.setValue(riddle.getDevices());
    }

    public Riddle getParentRiddle() {
        return parentRiddle;
    }


}
