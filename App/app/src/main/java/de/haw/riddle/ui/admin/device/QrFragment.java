package de.haw.riddle.ui.admin.device;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import de.haw.riddle.R;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.net.admin.DeviceService;
import de.haw.riddle.ui.admin.model.Device;
import de.haw.riddle.ui.admin.model.DeviceData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QrFragment extends DaggerFragment {

    private static final String TAG = QrFragment.class.getSimpleName();
    private CodeScanner mCodeScanner;
    private TextView qrScannerText;

    @Inject
    DeviceService deviceService;


    public static QrFragment newInstance() {
        return new QrFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_qr, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireContext(), scannerView);
        qrScannerText = view.findViewById(R.id.scanner_text);

        mCodeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> {

            Gson gson= new Gson();
                    DeviceData deviceData = gson.fromJson(result.getText(), DeviceData.class);
                    System.out.println(gson.toJson(deviceData));
                    deviceService.createDevice(deviceData).enqueue(new Callback<Device>() {
                        @Override
                        public void onResponse(Call<Device> call, Response<Device> response) {

                            if(response.isSuccessful())
                                Toast.makeText(requireContext(),"Device created",Toast.LENGTH_SHORT).show();
                            else {

                                try {
                                    final String errorBody;
                                    errorBody = response.errorBody().string();
                                    Toast.makeText(requireContext(),errorBody,Toast.LENGTH_SHORT).show();
                                    Log.e(TAG,errorBody);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }



                            }
                        }

                        @Override
                        public void onFailure(Call<Device> call, Throwable t) {

                        }
                    });

                    Log.i(TAG,deviceData.toString());
                    qrScannerText.setText(result.getText());
        }
        ));

        scannerView.setOnClickListener(view1 -> mCodeScanner.startPreview());


    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }


}
