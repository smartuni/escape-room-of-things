package de.haw.riddle;

import android.util.Log;

import androidx.navigation.NavController;

import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.CongratulationsWindow;
import de.haw.riddle.ui.admin.model.Riddle;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PullPuzzleStateRunnable implements Runnable{

    private final NavController navController;
    public static int activePuzzleId;
    private RiddleService riddleService;
    private int actionFragmentToCongrats;
    private int actionCongratsToFragment;
    private static final String TAG = PullPuzzleStateRunnable.class.getSimpleName();

    public PullPuzzleStateRunnable(NavController navController, RiddleService riddleService, int actionFragmentToCongrats, int actionCongratsToFragment) {
        this.navController = navController;
        this.riddleService = riddleService;
        this.actionFragmentToCongrats = actionFragmentToCongrats;
        this.actionCongratsToFragment = actionCongratsToFragment;
    }

    @Override
    public void run() {
        riddleService.getRiddles(activePuzzleId).enqueue(new Callback<Riddle>() {
            @Override
            public void onResponse(Call<Riddle> call, Response<Riddle> response) {
                Log.i(TAG, "Response Code = " + response.code());
                if (response.isSuccessful()) {
                    Riddle riddle = response.body();
                    Log.i(TAG, "Riddle: " + riddle);
                    if (riddle.getState().equalsIgnoreCase("solved")){
                        Log.i(TAG, "riddle.getState().equalsIgnoreCase(\"solved\")");
                        navController.navigate(actionFragmentToCongrats, CongratulationsWindow.createArgs(actionCongratsToFragment));

                    };
                }
            }

            @Override
            public void onFailure(Call<Riddle> call, Throwable t) {

            }
        });
    }
}
