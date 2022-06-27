package de.haw.riddle.ui.admin.device;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.util.SimpleTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

        Riddle[] items = viewModel.getPuzzleList();
        ArrayAdapter <Riddle> adapter = new ArrayAdapter<>(requireContext(),R.layout.dropdown_list_item,items);

        TextInputEditText tfSerial = view.findViewById(R.id.tfSerial);
        TextInputEditText tfName = view.findViewById(R.id.tfName);
        TextInputEditText tfPublicKey = view.findViewById(R.id.tfPublicKey);
        TextInputEditText tfDescription = view.findViewById(R.id.tfDescription);
        TextInputEditText tfDevIp = view.findViewById(R.id.tfDevIp);
        TextInputEditText tfIsEventDevice = view.findViewById(R.id.tfIsEventDevice);
        TextInputEditText tfNodeState = view.findViewById(R.id.tfNodeState);
        TextInputEditText tfId = view.findViewById(R.id.tfId);
        AutoCompleteTextView tfPuzzle = view.findViewById(R.id.tfPuzzle);
        TextInputEditText tfState = view.findViewById(R.id.tfState);

        tfPuzzle.setAdapter(adapter);

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
                tfPuzzle.setText(items[0].toString(),false);
                tfState.setText(device.getState());



        }
        //TextInputEditText tfName = view.findViewById(R.id.tfName);

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });


        tfPuzzle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                viewModel.setParentPuzzle(adapter.getItem(i));
                System.out.println(adapter.getItem(i));
            }
        }

    );


        Button applyBtn= view.findViewById(R.id.applyButton);

        applyBtn.setOnClickListener(view1 -> viewModel.updateDevice().enqueue(new Callback<Device>() {
            @Override
            public void onResponse(Call<Device> call, Response<Device> response) {
                if(response.isSuccessful())
                {
                    Toast.makeText(requireContext(),"Succesful",Toast.LENGTH_SHORT).show();
                }

                try {
                    if(response.errorBody()!=null)
                    Log.e(TAG,response.errorBody().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<Device> call, Throwable t) {

                progressLayout.setVisibility(View.INVISIBLE);
                Log.e(TAG, "Failed to create update device.", t);
//                        Toast.makeText(requireContext(), R.string.roomCreateFailed, Toast.LENGTH_SHORT).show();
                Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
            }
        }));




    }
}
