package de.haw.riddle.ui.admin.riddle;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.LiveData;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.ui.admin.room.RoomListAdapter;
import de.haw.riddle.ui.admin.room.RoomViewModel;

public class RiddleFragment extends DaggerFragment {

    public static final String KEY_ROOM = "Room";

    @Inject
    RiddleViewModel riddleViewModel;

    public static Bundle createArgs(Room room) {
        Bundle args = new Bundle();
        args.putParcelable(RiddleFragment.KEY_ROOM, room);
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Room room = requireArguments().getParcelable(KEY_ROOM);
        riddleViewModel.setRoom(room);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_puzzle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LiveData<List<Riddle>> riddles = riddleViewModel.getRiddle();
        final RiddleListAdapter adapter = new RiddleListAdapter(riddles.getValue(), NavHostFragment.findNavController(this),riddleViewModel.getParentRoom().getId());
        riddles.observe(getViewLifecycleOwner(), adapter::updateRiddle);

        final RecyclerView recyclerView = view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.list_item_divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        Button btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_fragmentPuzzle_to_fragment_riddle_detail));


        final SwipeRefreshLayout swipeRefreshLayout = view.findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(() -> riddleViewModel.sync(swipeRefreshLayout));
    }
}
