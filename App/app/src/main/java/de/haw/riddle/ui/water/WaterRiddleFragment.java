package de.haw.riddle.ui.water;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.MainActivity;
import de.haw.riddle.PullPuzzleStateRunnable;
import de.haw.riddle.R;
import de.haw.riddle.net.admin.RiddleService;
import de.haw.riddle.ui.CongratulationsWindow;
import de.haw.riddle.util.Preferences;

public class WaterRiddleFragment extends DaggerFragment implements PullPuzzleStateRunnable.Callback {

    private static final String TAG = WaterRiddleFragment.class.getSimpleName();

    private final ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private final Queue<String> tips = new LinkedList<>();
    private final TipsListAdapter adapter = new TipsListAdapter();
    private Button btnTip;
    private ScheduledFuture<?> scheduledFuture;
    private PullPuzzleStateRunnable task;

    @Inject
    RiddleService riddleService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tips.add("Fill the 0.3l bottle with water.");
        tips.add("Pour your 0.3l into the 0.5l bottle. \nThen fill the 0.3l bottle again an \nuse it to fill the 0.5l.");
        tips.add("Empty the 0.5l bottle and pour the \n0.3l bottle’s remaining 0.1 liter in. \nRefill the 0.3l bottle and pour it into \nthe 0.5l bottle. ");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_water, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.list_tip);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnTip = view.findViewById(R.id.btnTip);
        btnTip.setOnClickListener(view1 -> new AlertDialog.Builder(requireContext())
                .setTitle("Are you sure you need a tip?")
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.confirm, (dialogInterface, i) -> showNextTip())
                .create()
                .show());

        final Button congrats = view.findViewById(R.id.congrats);
        congrats.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_fragmentWaterRiddle_to_congratulationsWindow, CongratulationsWindow.createArgs(R.id.action_congratulationsWindow_to_legoRiddleFragmentPart1));
            ((MainActivity) requireActivity()).showDrawerAndMenu();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "Schedule riddle state pull");
        final int idRiddleWater = PreferenceManager.getDefaultSharedPreferences(requireContext()).getInt(Preferences.ID_RIDDLE_WATER, 0);
        task = new PullPuzzleStateRunnable(riddleService, idRiddleWater, this);
        scheduledFuture = scheduledExecutorService.scheduleAtFixedRate(task, 1, 3, TimeUnit.SECONDS);
//        scheduledFuture = scheduledExecutorService.schedule(new PullPuzzleStateRunnable(NavHostFragment.findNavController(this), riddleService, R.id.action_fragmentWaterRiddle_to_congratulationsWindow, R.id.action_congratulationsWindow_to_legoRiddleFragmentPart1), 2, TimeUnit.SECONDS);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "Cancel riddle state pull");
        task.cancel();
        scheduledFuture.cancel(true);
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    private void showNextTip() {
        adapter.addTip(tips.poll());
        if (tips.isEmpty())
            btnTip.setEnabled(false);
    }

    @Override
    public void onPuzzleStateSolved() {
        Log.i(TAG, "onPuzzleSolved()");
        task.cancel();
        scheduledFuture.cancel(true);
        Log.i(TAG, "Show congrats window");
        NavHostFragment.findNavController(this).navigate(R.id.congratulationsWindow, CongratulationsWindow.createArgs(R.id.legoRiddleFragmentPart1));
    }
}
