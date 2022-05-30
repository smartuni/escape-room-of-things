package de.haw.riddle.ui.led;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import de.haw.riddle.R;
import de.haw.riddle.net.led.LedService;
import de.haw.riddle.net.led.LedStatus;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LedFragment extends DaggerFragment {

    private static final String TAG= LedFragment.class.getSimpleName();
    private MainViewModel mViewModel;
    private Button ledButton;
    @Inject
    LedService ledService;
    private boolean led0IsOn=false;

    public static LedFragment newInstance() {
        return new LedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_led, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        ledButton= view.findViewById(R.id.ledButton);
        System.out.println("ledService = " + ledService);
        ledService.getLedStatus(1).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i(TAG,"Response Body = "+response.body());
                if(response.body().equals("0"))
                {
                    ledButton.setBackgroundResource(R.drawable.led_button_off);

                }
                else
                {
                    ledButton.setBackgroundResource(R.drawable.led_button_on);
                    led0IsOn=true;
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e(TAG, "Failed to get Led Status",t);
            }
        });
        ledButton.setOnClickListener(view1 -> {
            if(led0IsOn){
                ledService.setLedStatus(0, LedStatus.deactivate()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ledButton.setBackgroundResource(R.drawable.led_button_off);
                        Log.i(TAG,"Led0 deactivated");
                        led0IsOn=false;
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG,"Failed to deactivate Led0",t);
                    }
                });
            }
            else
            {
                ledService.setLedStatus(0, LedStatus.activate()).enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        ledButton.setBackgroundResource(R.drawable.led_button_on);
                        Log.i(TAG,"Led0 activated");
                        led0IsOn=true;
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Log.e(TAG,"Failed to activate Led0",t);
                    }
                });
            }

        });
    }

}