package de.haw.riddle.net.admin;

import java.util.List;

import de.haw.riddle.net.ApiResponse;
import de.haw.riddle.ui.admin.model.Riddle;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RiddleService {
    @GET("/puzzles")
    Call<ApiResponse<List<Riddle>>> getRiddles();

    @GET("/puzzles/{id}")
    Call<Riddle> getRiddles(@Path("id") long id);

    @POST("/puzzles")
    Call<Riddle> createRiddle(@Body CreateRiddleDto riddle);

    @PUT("/puzzles")
    Call<Riddle> updateRiddle(@Body Riddle riddle);

    @DELETE("/puzzles/{id}")
    Call<Riddle> deleteRiddle(@Path("id") long id);
}
