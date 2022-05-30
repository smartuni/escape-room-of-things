package de.haw.riddle.ui.qr;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import de.haw.riddle.R;

import dagger.android.support.DaggerFragment;

public class QrFragment extends DaggerFragment {

    private CodeScanner mCodeScanner;
    private TextView qrScannerText;


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

        mCodeScanner.setDecodeCallback(result -> requireActivity().runOnUiThread(() -> qrScannerText.setText(result.getText())));
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
