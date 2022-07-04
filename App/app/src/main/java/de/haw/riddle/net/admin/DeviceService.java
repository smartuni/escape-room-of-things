package de.haw.riddle.net.admin;

import java.util.List;

import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.DeviceData;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.model.UpdateDeviceDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface DeviceService {
    @GET("/coap/device")
    Call<List<Device>> getDevice();

    @GET("/coap/device{id}")
    Call<Device> getDeviceById(@Path("id") int id);

    @POST("/devices")
    Call<Device> createDevice(@Body DeviceData device);

    @PUT("/devices/{id}")
    Call<Device> updateDevice(@Path("id") long id,@Body UpdateDeviceDto device);

    @DELETE("/coap/device")
    Call<Device> deleteDevice(@Body Device device);
}