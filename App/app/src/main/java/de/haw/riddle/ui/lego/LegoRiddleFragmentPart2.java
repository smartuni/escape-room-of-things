package de.haw.riddle.ui.lego;

import android.os.Bundle;
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
import de.haw.riddle.ui.water.TipsListAdapter;

public class LegoRiddleFragmentPart2 extends DaggerFragment {

    private final Queue<String> tips = new LinkedList<>();
    private final TipsListAdapter adapter = new TipsListAdapter();
    private Button btnTip;
    private ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(1);
    private ScheduledFuture scheduledFuture;

    @Inject
    RiddleService riddleService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tips.add("The biggest orange brick is a part of the alien’s head.");
        tips.add("The round bricks are the eyes and the yellow brick is the mouth.");
        tips.add("You should place the yellow brick the way you can read \"LEGO\" from it");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lego_part2, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.list_tip2);
        recyclerView.setAdapter(adapter);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        btnTip = view.findViewById(R.id.btnTip2);
        btnTip.setOnClickListener(view1 -> new AlertDialog.Builder(requireContext())
                .setTitle("Are you sure you need a tip?")
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.cancel())
                .setPositiveButton(R.string.confirm, (dialogInterface, i) -> showNextTip())
                .create()
                .show());

        final Button congrats = view.findViewById(R.id.congrats);
        congrats.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_legoRiddleFragmentPart2_to_congratulationsWindow, CongratulationsWindow.createArgs(R.id.action_legoRiddleFragmentPart2_to_congratulationsWindow));
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
        scheduledFuture = scheduledExecutorService.schedule(new PullPuzzleStateRunnable(NavHostFragment.findNavController(this), riddleService, R.id.action_legoRiddleFragmentPart2_to_congratulationsWindow, R.id.action_congratulationsWindow_to_ledRiddleFragment), 2, TimeUnit.SECONDS);
    }

    @Override
    public void onPause() {
        super.onPause();
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

}
