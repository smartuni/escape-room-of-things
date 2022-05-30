package de.haw.riddle.ui.admin.riddle;

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
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.ui.admin.room.RoomDetailViewModel;
import de.haw.riddle.ui.admin.room.RoomViewModel;
import de.haw.riddle.util.SimpleTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiddleDetailFragment extends DaggerFragment {

    public static final String TAG = RiddleDetailViewModel.class.getSimpleName();
    private static final String KEY_RIDDLE = "riddle";

    private View progressLayout;

    @Inject
    RiddleViewModel riddleViewModel;
    @Inject
    RiddleDetailViewModel viewModel;

    public static Bundle createArgs(@Nullable Riddle riddle) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_RIDDLE, riddle);
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_room_detail, container, false);
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
            viewModel.setRiddle(args.getParcelable(KEY_RIDDLE));
        }
        TextInputEditText tfName = view.findViewById(R.id.tfName);

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });

        view.findViewById(R.id.applyButton).setOnClickListener(v -> {
            final Call<Riddle> call = viewModel.createRiddleCallIfValid();
            if (call == null)
                Toast.makeText(requireContext(), R.string.roomInputNotValid, Toast.LENGTH_LONG).show();
            else {
                progressLayout.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Riddle>() {
                    @Override
                public void onResponse(@NonNull Call<Riddle> call, @NonNull Response<Riddle> response) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "Successfully posted room to server.\nResponseBody=\n" + response.body());
                        if (args == null)
                            Toast.makeText(requireContext(), R.string.roomCreateSuccess, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(requireContext(), R.string.roomUpdateSuccess, Toast.LENGTH_SHORT).show();

                        riddleViewModel.addRiddle(response.body());
                        NavHostFragment.findNavController(RiddleDetailFragment.this).navigateUp();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Riddle> call, @NonNull Throwable t) {
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
