package de.haw.riddle.net.admin;

import java.util.List;

import de.haw.riddle.net.ApiResponse;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.model.UpdateStateDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface RoomService {
    @GET("/rooms")
    Call<ApiResponse<List<Room>>> getRooms();

    @GET("/rooms/{id}")
    Call<Room> getRoomById(@Path("id") long id);

    @POST("/rooms")
    Call<Room> createRoom(@Body CreateRoomDto room);

    @PUT("/rooms/state/{id}")
    Call<Room> updateRoomState(@Path("id") long id,@Body UpdateStateDto state);

    @DELETE("/rooms/{id}")
    Call<Room> deleteRoom(@Path("id") long id);

}
