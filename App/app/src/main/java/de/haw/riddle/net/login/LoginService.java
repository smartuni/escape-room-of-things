package de.haw.riddle.net.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface LoginService {

    @POST("/login")
    Call<Token> login(@Body User user);
}
