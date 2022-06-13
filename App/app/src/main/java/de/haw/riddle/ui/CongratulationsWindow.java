package de.haw.riddle.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import de.haw.riddle.MainActivity;
import de.haw.riddle.R;
import de.haw.riddle.util.Preferences;


public class CongratulationsWindow extends DialogFragment {

    public static final String TAG = CongratulationsWindow.class.getSimpleName();

    public static CongratulationsWindow newInstance() {
        return new CongratulationsWindow();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View adminView = inflater.inflate(R.layout.fragment_congratulations, null);

        final Button btnConfirm = adminView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(R.id.action_congratulationsWindow_to_legoRiddleFragment);
            ((MainActivity) requireActivity()).showDrawerAndMenu();
        });

        builder.setView(adminView);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
