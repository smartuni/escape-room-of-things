package de.haw.riddle;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.io.IOException;
import java.util.concurrent.Executor;

import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.admin.model.Riddle;
import lombok.RequiredArgsConstructor;
import retrofit2.Response;

@RequiredArgsConstructor
public class PullPuzzleStateRunnable implements Runnable {

    private static final String TAG = PullPuzzleStateRunnable.class.getSimpleName();
    private static final MainThreadExecutor MAIN_THREAD_EXECUTOR = new MainThreadExecutor();


    private final RiddleService riddleService;
    private final int riddleId;
    private final Callback callback;
    private boolean isCanceled = false;

    public interface Callback {
        void onPuzzleStateSolved();
    }

    @Override
    public void run() {
        Log.i(TAG, "getRiddleState() for riddle with id " + riddleId);
        try {
            final Response<Riddle> response = riddleService.getRiddles(riddleId).execute();
            Log.i(TAG, "Response Code = " + response.code());
            if (response.isSuccessful()) {
                Riddle riddle = response.body();
                Log.i(TAG, "Riddle: " + riddle);
                if (riddle.getState().equalsIgnoreCase("solved")) {
                    Log.i(TAG, "riddle.getState().equalsIgnoreCase(\"solved\")");
                    if (!isCanceled)
                        MAIN_THREAD_EXECUTOR.execute(callback::onPuzzleStateSolved);
                    else
                        Log.i(TAG, "Canceled...");
                }
            }
        } catch (IOException e) {
            Log.e(TAG, "Failed to pull riddle state", e);
        }
    }

    public void cancel() {
        isCanceled = true;
    }


    public static class MainThreadExecutor implements Executor {
        private final Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }
}
