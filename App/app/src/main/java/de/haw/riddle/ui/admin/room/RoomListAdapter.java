package de.haw.riddle.ui.admin.room;

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
import de.haw.riddle.ui.admin.riddle.RiddleFragment;
import de.haw.riddle.ui.admin.model.Room;
import lombok.Getter;

import java.util.List;

public class RoomListAdapter extends RecyclerView.Adapter<RoomListAdapter.ViewHolder> {

    private final NavController navController;
    private List<Room> rooms;


    public RoomListAdapter(List<Room> rooms, NavController navController) {
        this.rooms = rooms;
        this.navController = navController;
    }

    public void updateRooms(@NonNull List<Room> updatedRooms) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return rooms.size();
            }

            @Override
            public int getNewListSize() {
                return updatedRooms.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return rooms.get(oldItemPosition).getId() == updatedRooms.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return rooms.get(oldItemPosition).equals(updatedRooms.get(newItemPosition));
            }
        });
        rooms = updatedRooms;
        diffResult.dispatchUpdatesTo(this);
    }

//    public void addHint(String hint){
//        rooms.add(hint);
//        notifyDataSetChanged();
//    }

    @Override
    public long getItemId(int position) {
        return rooms.get(position).getId();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_room_list, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        final Room room = rooms.get(position);
        viewHolder.getTextView().setText(room.getName());


        viewHolder.itemView.setOnClickListener(view -> navController.navigate(R.id.action_fragmentRoom_to_fragmentRiddle, RiddleFragment.createArgs(room)));
        viewHolder.getSettingsButton().setOnClickListener(view -> navController.navigate(R.id.action_fragmentRoom_to_fragmentRoomDetail, RoomDetailFragment.createArgs(rooms.get(position))));
    }

    @Override
    public int getItemCount() {
        return rooms.size();
    }


    @Getter
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;
        private final ImageButton settingsButton;

        public ViewHolder(View view) {
            super(view);
            textView = view.findViewById(R.id.textView);
            settingsButton = view.findViewById(R.id.settingsButton);
        }
    }
}


