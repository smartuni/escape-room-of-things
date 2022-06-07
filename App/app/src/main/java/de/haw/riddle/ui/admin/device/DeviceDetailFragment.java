package de.haw.riddle.ui.admin.device;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.util.SimpleTextWatcher;

public class DeviceDetailFragment extends DaggerFragment {

    public static final String TAG = DeviceDetailViewModel.class.getSimpleName();
    private static final String KEY_DEVICE = "device";
    @Inject
    DeviceViewModel deviceViewModel;
    @Inject
    DeviceDetailViewModel viewModel;
    private View progressLayout;

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

        TextInputEditText tfSerial = view.findViewById(R.id.tfSerial);
        TextInputEditText tfName = view.findViewById(R.id.tfName);
        TextInputEditText tfPublicKey = view.findViewById(R.id.tfPublicKey);
        TextInputEditText tfDescription = view.findViewById(R.id.tfDescription);
        TextInputEditText tfDevIp = view.findViewById(R.id.tfDevIp);
        TextInputEditText tfIsEventDevice = view.findViewById(R.id.tfIsEventDevice);
        TextInputEditText tfNodeState = view.findViewById(R.id.tfNodeState);
        TextInputEditText tfId = view.findViewById(R.id.tfId);
        TextInputEditText tfPuzzle = view.findViewById(R.id.tfPuzzle);
        TextInputEditText tfState = view.findViewById(R.id.tfState);


        Bundle args = getArguments();
        if (args != null) {
            System.out.println("args = " + args.getParcelable(KEY_DEVICE));
            view.findViewById(R.id.tlId).setVisibility(View.VISIBLE);
            viewModel.setDevice(args.getParcelable(KEY_DEVICE));
            final Device device = args.getParcelable(KEY_DEVICE);
            tfSerial.setText(device.getSerial());
            tfName.setText(device.getName());
            tfPublicKey.setText(device.getPublicKey());
            if (device.getDescription() != null)
                tfDescription.setText(device.getDescription());
            tfDevIp.setText(String.valueOf(device.getDevIP()));
            tfIsEventDevice.setText(String.valueOf(device.isEventDevice()));
            tfNodeState.setText(device.getNodeState());
            tfId.setText(String.valueOf(device.getId()));
            tfPuzzle.setText(String.valueOf(device.getParentPuzzleId()));
            tfState.setText(device.getState());

        }
        //TextInputEditText tfName = view.findViewById(R.id.tfName);

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });


    }
}
