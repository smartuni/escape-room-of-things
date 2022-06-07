package de.haw.riddle.ui.admin.riddle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import de.haw.riddle.R;
import de.haw.riddle.ui.admin.device.DeviceFragment;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.room.RoomDetailFragment;
import lombok.Getter;

import java.util.List;

public class RiddleListAdapter extends RecyclerView.Adapter<RiddleListAdapter.ViewHolder> {


    private final NavController navController;
    private  List<Riddle> puzzles;
    private final long parentRoomId;

    public RiddleListAdapter(List<Riddle> puzzles, NavController navController, long parentRoomId) {
        this.puzzles = puzzles;
        this.navController = navController;
        this.parentRoomId= parentRoomId;
    }

    public void updateRiddle(@NonNull List<Riddle> updatedRiddles) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return puzzles.size();
            }

            @Override
            public int getNewListSize() {
                return updatedRiddles.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return puzzles.get(oldItemPosition).getId() == updatedRiddles.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return puzzles.get(oldItemPosition).equals(updatedRiddles.get(newItemPosition));
            }


        });

        puzzles = updatedRiddles;
        diffResult.dispatchUpdatesTo(this);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_room_list, viewGroup, false);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        final Riddle riddle = puzzles.get(position);
        viewHolder.getTextView().setText(riddle.getName());



        viewHolder.itemView.setOnClickListener(view -> navController.navigate(R.id.action_fragmentPuzzle_to_fragmentDevice, DeviceFragment.createArgs(riddle)));
        viewHolder.getSettingsButton().setOnClickListener(view -> navController.navigate(R.id.action_fragmentPuzzle_to_fragment_riddle_detail, RiddleDetailFragment.createArgs(riddle,parentRoomId)));

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return puzzles.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton settingsButton;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = view.findViewById(R.id.textView);
            settingsButton = view.findViewById(R.id.settingsButton);
        }
    }
}


