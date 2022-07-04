package de.haw.riddle.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.navigation.fragment.NavHostFragment;

import de.haw.riddle.MainActivity;
import de.haw.riddle.R;


public class CongratulationsWindow extends DialogFragment {

    public static final String TAG = CongratulationsWindow.class.getSimpleName();
    private static final String KEY_ID_ACTION = "next_fragment";
    private static final String KEY_IS_FINAL = "isFinal";

    public static Bundle createArgs(int idAction) {
        Bundle args = new Bundle();
        args.putInt(KEY_ID_ACTION, idAction);
        return args;
    }

    public static Bundle createArgs(int idAction, boolean isFinal) {
        Bundle args = new Bundle();
        args.putInt(KEY_ID_ACTION, idAction);
        args.putBoolean(KEY_IS_FINAL, isFinal);
        return args;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Log.i(TAG, "onCreateDialog");
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = requireActivity().getLayoutInflater();

        final View adminView = inflater.inflate(R.layout.fragment_congratulations, null);

        final int idAction = requireArguments().getInt(KEY_ID_ACTION);

        final Button btnConfirm = adminView.findViewById(R.id.btnConfirm);

        if (requireArguments().getBoolean(KEY_IS_FINAL, false)) {
            adminView.findViewById(R.id.tvcongratilations).setVisibility(View.GONE);
            ((TextView) adminView.findViewById(R.id.stub1_text2)).setText("Congratulations! \nYou solved all riddles and escaped the room!");
            btnConfirm.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigate(R.id.fragmentInfo);
                ((MainActivity) requireActivity()).showDrawerAndMenu();
            });
        } else {
            btnConfirm.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigate(idAction);
                ((MainActivity) requireActivity()).showDrawerAndMenu();
            });
        }


        builder.setView(adminView);
        Dialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

}
