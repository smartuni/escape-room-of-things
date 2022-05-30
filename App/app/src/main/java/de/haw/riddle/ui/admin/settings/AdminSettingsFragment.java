package de.haw.riddle.ui.admin.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import de.haw.riddle.R;
import de.haw.riddle.util.Preferences;
import com.google.android.material.textfield.TextInputEditText;

import java.util.regex.Pattern;

import dagger.android.support.DaggerFragment;

public class AdminSettingsFragment extends DaggerFragment {

    private static final Pattern REGEX_IP = Pattern.compile("^(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)$");
    private static final Pattern REGEX_PORT = Pattern.compile("^([1-9][0-9]{0,3}|[1-5][0-9]{4}|6[0-4][0-9]{3}|65[0-4][0-9]{2}|655[0-2][0-9]|6553[0-5])$");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        Button applyButton = view.findViewById(R.id.applyButton);
        TextInputEditText ipAddress = view.findViewById(R.id.ipTextField);
        TextInputEditText port = view.findViewById(R.id.portTextField);

        ipAddress.setText(preferences.getString(Preferences.IP_ADDRESS, null));
        port.setText(preferences.getString(Preferences.PORT, null));


        applyButton.setOnClickListener(view1 -> {
            String ipText = ipAddress.getText() == null ? "" : ipAddress.getText().toString();
            String portText = port.getText() == null ? "" : port.getText().toString();
            boolean isValidIp = REGEX_IP.matcher(ipText).find();
            boolean isValidPort = REGEX_PORT.matcher(portText).find();

            if (!REGEX_IP.matcher(ipText).find())
                Toast.makeText(requireContext(), "IP-Address not valid", Toast.LENGTH_LONG).show();
            if (!REGEX_PORT.matcher(portText).find())
                Toast.makeText(requireContext(), "Port not valid", Toast.LENGTH_LONG).show();
            if (isValidIp && isValidPort)
                preferences.edit()
                        .putString(Preferences.IP_ADDRESS, ipText)
                        .putString(Preferences.PORT, portText)
                        .apply();
        });

    }

}
