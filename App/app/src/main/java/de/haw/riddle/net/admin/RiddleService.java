package de.haw.riddle.net.admin;

import java.util.List;

import de.haw.riddle.ui.admin.model.Riddle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RiddleService {
    @GET("/riddle")
    Call<List<Riddle>> getRiddles();

    @GET("/riddle{id}")
    Call<Riddle> getRiddleById(@Path("id") int id);

    @POST("/riddle")
    Call<Riddle> createRiddle(@Body Riddle riddle);

    @PUT("/riddle")
    Call<Riddle> updateRiddle(@Body Riddle riddle);

    @DELETE("/riddle")
    Call<Riddle> deleteRiddle(@Body Riddle riddle);
}
