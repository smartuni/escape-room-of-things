package de.haw.riddle.ui.admin.device;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.riddle.RiddleDetailViewModel;
import de.haw.riddle.ui.admin.riddle.RiddleViewModel;
import de.haw.riddle.ui.admin.room.RoomDetailViewModel;
import de.haw.riddle.util.SimpleTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DeviceDetailFragment extends DaggerFragment {

    public static final String TAG = DeviceDetailViewModel.class.getSimpleName();
    private static final String KEY_DEVICE = "device";

    private View progressLayout;

    @Inject
    DeviceViewModel deviceViewModel;
    @Inject
    DeviceDetailViewModel viewModel;

    public static Bundle createArgs(@Nullable Device device) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_DEVICE, device);
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_device_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressLayout = view.findViewById(R.id.progressLayout);
        progressLayout.setOnTouchListener((v, event) -> {
            v.performClick();
            return true;
        });

        Bundle args = getArguments();
        if (args != null) {
            view.findViewById(R.id.tlId).setVisibility(View.VISIBLE);
            viewModel.setDevice(args.getParcelable(KEY_DEVICE));
        }
        TextInputEditText tfName = view.findViewById(R.id.tfName);

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });

        view.findViewById(R.id.applyButton).setOnClickListener(v -> {
            final Call<Device> call = viewModel.createDeviceCallIfValid();
            if (call == null)
                Toast.makeText(requireContext(), R.string.roomInputNotValid, Toast.LENGTH_LONG).show();
            else {
                progressLayout.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Device>() {
                    @Override
                public void onResponse(@NonNull Call<Device> call, @NonNull Response<Device> response) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "Successfully posted room to server.\nResponseBody=\n" + response.body());
                        if (args == null)
                            Toast.makeText(requireContext(), R.string.roomCreateSuccess, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(requireContext(), R.string.roomUpdateSuccess, Toast.LENGTH_SHORT).show();

                        deviceViewModel.addDevice(response.body());
                        NavHostFragment.findNavController(DeviceDetailFragment.this).navigateUp();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Device> call, @NonNull Throwable t) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Failed to create room.", t);
//                        Toast.makeText(requireContext(), R.string.roomCreateFailed, Toast.LENGTH_SHORT).show();
                        Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}
