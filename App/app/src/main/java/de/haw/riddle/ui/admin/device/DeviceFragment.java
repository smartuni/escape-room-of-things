package de.haw.riddle.ui.admin.device;

import android.os.Bundle;
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

import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.riddle.RiddleListAdapter;

public class DeviceFragment extends DaggerFragment {

    public static final String KEY_RIDDLE = "Riddle";
    @Inject
    DeviceViewModel viewModel;

    public static Bundle createArgs(Riddle riddle) {
        Bundle args = new Bundle();
        args.putParcelable(DeviceFragment.KEY_RIDDLE, riddle);
        return args;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel.setRiddle(requireArguments().getParcelable(KEY_RIDDLE));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final LiveData<List<Device>> devices = viewModel.getDevice();
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);


        Riddle riddle = requireArguments().getParcelable(KEY_RIDDLE);
        DeviceListAdapter adapter = new DeviceListAdapter(devices.getValue(), NavHostFragment.findNavController(this));
        devices.observe(getViewLifecycleOwner(), adapter::updateDevices);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);

        final DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(requireContext(), layoutManager.getOrientation());
        dividerItemDecoration.setDrawable(Objects.requireNonNull(ContextCompat.getDrawable(requireContext(), R.drawable.list_item_divider)));
        recyclerView.addItemDecoration(dividerItemDecoration);

        Button btnAdd = view.findViewById(R.id.btnQr);
        btnAdd.setOnClickListener(v -> NavHostFragment.findNavController(this).navigate(R.id.action_fragmentDevice_to_fragmentQr));


    }
}
