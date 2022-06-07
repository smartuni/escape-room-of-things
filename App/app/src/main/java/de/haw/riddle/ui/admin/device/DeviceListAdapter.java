package de.haw.riddle.ui.admin.device;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.riddle.RiddleDetailFragment;

public class DeviceListAdapter extends RecyclerView.Adapter<DeviceListAdapter.ViewHolder> {


    private final NavController navController;
    private  List<Device> devices;


    public DeviceListAdapter(List<Device> devices, NavController navController) {
        this.devices = devices;
        this.navController = navController;
    }


    public void updateDevices(@NonNull List<Device> updateDevices) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return devices.size();
            }

            @Override
            public int getNewListSize() {
                return updateDevices.size();
            }

            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                return devices.get(oldItemPosition).getId() == updateDevices.get(newItemPosition).getId();
            }

            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                return devices.get(oldItemPosition).equals(updateDevices.get(newItemPosition));
            }
        });
        devices = updateDevices;
        diffResult.dispatchUpdatesTo(this);
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // Create a new view, which defines the UI of the list item
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_room_list, viewGroup, false);

        view.findViewById(R.id.settingsButton).setVisibility(View.GONE);

        return new ViewHolder(view);
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        //viewHolder.getTextView().setText(device.get(position).getDevice());

        final Device device = devices.get(position);
        viewHolder.getTextView().setText(devices.get(position).getName());
        viewHolder.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 navController.navigate(R.id.action_fragmentDevice_to_fragment_device_detail, DeviceDetailFragment.createArgs(device));

            }
        });



    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return devices.size();
    }

    /**
     * Provide a reference to the type of views that you are using
     * (custom ViewHolder).
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView textView;

        public ViewHolder(View view) {
            super(view);
            // Define click listener for the ViewHolder's View

            textView = (TextView) view.findViewById(R.id.textView);
        }

        public TextView getTextView() {
            return textView;
        }
    }
}


