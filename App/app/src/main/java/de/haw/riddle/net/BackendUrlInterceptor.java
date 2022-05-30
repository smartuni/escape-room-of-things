package de.haw.riddle.net;

import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import de.haw.riddle.util.Preferences;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class BackendUrlInterceptor implements Interceptor {

    public static final String TAG = BackendUrlInterceptor.class.getSimpleName();

    private final SharedPreferences prefs;

    public BackendUrlInterceptor(SharedPreferences prefs) {
        this.prefs = prefs;
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request request = chain.request();

        request = request.newBuilder()
                .url(request.url().newBuilder()
                        .scheme("http")
                        .host(prefs.getString(Preferences.IP_ADDRESS, "127.0.0.1"))
                        .port(Integer.parseInt(prefs.getString(Preferences.PORT, "5000")))
                        .build())
                .build();

        Log.i(TAG, request.toString());
        return chain.proceed(request);
    }
}
