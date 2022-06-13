package de.haw.riddle.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import de.haw.riddle.MainActivity;
import de.haw.riddle.R;


public class CongratulationsWindow extends DialogFragment {

    public static final String TAG = CongratulationsWindow.class.getSimpleName();
    private static final String KEY_ID_ACTION = "next_fragment";

    public static Bundle createArgs(int idAction) {

        Bundle args = new Bundle();
        args.putInt(KEY_ID_ACTION, idAction);
        return args;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View adminView = inflater.inflate(R.layout.fragment_congratulations, null);

        final int idAction = requireArguments().getInt(KEY_ID_ACTION);

        final Button btnConfirm = adminView.findViewById(R.id.btnConfirm);
        btnConfirm.setOnClickListener(v -> {
            NavHostFragment.findNavController(this).navigate(idAction);
            ((MainActivity) requireActivity()).showDrawerAndMenu();
        });

        builder.setView(adminView);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
