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

import dagger.android.support.DaggerFragment;
import de.haw.riddle.MainActivity;
import de.haw.riddle.R;
import de.haw.riddle.ui.CongratulationsWindow;
import de.haw.riddle.ui.water.TipsListAdapter;

public class LegoRiddleFragmentPart2 extends DaggerFragment {

    private final Queue<String> tips = new LinkedList<>();
    private final TipsListAdapter adapter = new TipsListAdapter();
    private Button btnTip;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tips.add("text");
        tips.add("text");
        tips.add("text");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_lego_part2, container, false);
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
        // ToDo: pull
    }

    @Override
    public void onPause() {
        super.onPause();
        // ToDo: pull stoppen
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
