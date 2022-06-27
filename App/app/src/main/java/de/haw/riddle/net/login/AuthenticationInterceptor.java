package de.haw.riddle.net.login;

import android.content.SharedPreferences;

import java.io.IOException;

import de.haw.riddle.util.Preferences;
import okhttp3.Authenticator;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;

public class AuthenticationInterceptor implements Authenticator {

    private final SharedPreferences prefs;

    public AuthenticationInterceptor(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @Override
    public Request authenticate(Route route, Response response) {
        return response.request().newBuilder()
                .header("x-access-token", prefs.getString(Preferences.TOKEN, ""))
                .build();
    }
}
