package de.haw.riddle.net.led;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LedService {
    @POST("/coap/led{id}")
    Call<Void> setLedStatus(@Path("id") int id, @Body LedStatus ledStatus);

    @GET("/coap/led{id}")
    Call<String> getLedStatus(@Path("id") int id);
}
