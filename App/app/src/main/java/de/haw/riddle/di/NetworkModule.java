package de.haw.riddle.di;

import android.content.Context;
import android.preference.PreferenceManager;

import de.haw.riddle.net.BackendUrlInterceptor;
import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.net.admin.RoomService;
import de.haw.riddle.net.led.LedService;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class NetworkModule {

    @Provides
    @Singleton
    Retrofit provideRetrofit(Context context) {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new BackendUrlInterceptor(PreferenceManager.getDefaultSharedPreferences(context)))
                .build();

        return new Retrofit.Builder()
                .baseUrl("http://127.0.0.1:5000")//TODO insert real backend url here
                .client(httpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    LedService provideLedService(Retrofit retrofit) {
        return retrofit.create(LedService.class);
    }


    @Singleton
    @Provides
    DeviceService provideDeviceService(Retrofit retrofit) {
        return retrofit.create(DeviceService.class);
    }

    @Singleton
    @Provides
    RoomService provideRoomService(Retrofit retrofit) {
        return retrofit.create(RoomService.class);
    }

    @Singleton
    @Provides
    RiddleService provideRiddleService(Retrofit retrofit) {
        return retrofit.create(RiddleService.class);
    }
}
