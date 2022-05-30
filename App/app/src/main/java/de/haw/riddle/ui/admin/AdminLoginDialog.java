package de.haw.riddle.ui.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;

import de.haw.riddle.R;
import de.haw.riddle.util.Preferences;


public class AdminLoginDialog extends DialogFragment {

    public static final String TAG = AdminLoginDialog.class.getSimpleName();
    private static final String ADMIN_PASSWORD = "Riot!";

    public static AdminLoginDialog newInstance() {
        return new AdminLoginDialog();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // Get the layout inflater
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View adminView = inflater.inflate(R.layout.fragment_admin_login, null);
        builder.setView(adminView)
                .setPositiveButton(R.string.login, (dialog, id) -> {
                    EditText etPassword = adminView.findViewById(R.id.password);
                    String password = etPassword.getText().toString();

                    if (password.equals(ADMIN_PASSWORD)) {
                        SharedPreferences pm = PreferenceManager.getDefaultSharedPreferences(getContext());
                        pm.edit().putBoolean(Preferences.IS_ADMIN, true).apply();
                        Toast.makeText(getContext(), R.string.loginSuccesfull, Toast.LENGTH_LONG).show();
                    } else
                        Toast.makeText(getContext(), R.string.loginFail, Toast.LENGTH_LONG).show();

                }).setNegativeButton(R.string.cancel, (dialog, id) -> AdminLoginDialog.this.requireDialog().cancel());
        return builder.create();
    }

}
