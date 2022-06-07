package de.haw.riddle.ui.admin.room;

import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.material.textfield.TextInputEditText;

import java.io.IOException;

import javax.inject.Inject;

import dagger.android.support.DaggerFragment;
import de.haw.riddle.R;
import de.haw.riddle.ui.admin.model.Room;
import de.haw.riddle.util.SimpleTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RoomDetailFragment extends DaggerFragment {

    public static final String TAG = RoomDetailViewModel.class.getSimpleName();
    private static final String KEY_ROOM = "room";

    private View progressLayout;

    @Inject
    RoomViewModel roomViewModel;
    @Inject
    RoomDetailViewModel viewModel;

    public static Bundle createArgs(@Nullable Room room) {
        Bundle args = new Bundle();
        args.putParcelable(KEY_ROOM, room);
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
        TextInputEditText tfName = view.findViewById(R.id.tfName);
        TextInputEditText tfDescription = view.findViewById(R.id.tfDescription);


        progressLayout = view.findViewById(R.id.progressLayout);
        progressLayout.setOnTouchListener((v, event) -> {
            v.performClick();
            return true;
        });

        Bundle args = getArguments();
        if (args != null) {
            final Room room = args.getParcelable(KEY_ROOM);
            viewModel.setRoom(room);
            view.findViewById(R.id.tlId).setVisibility(View.VISIBLE);
            ((TextInputEditText) view.findViewById(R.id.tfId)).setText(String.valueOf(room.getId()));
            tfName.setText(room.getName());
            tfDescription.setText(room.getDescription());
        }

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });
        ((TextInputEditText) view.findViewById(R.id.tfState)).setText(viewModel.getState());

        tfDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setDescription(s.toString());
            }
        });

        view.findViewById(R.id.applyButton).setOnClickListener(v -> {
            final Call<Room> call = viewModel.createRoomCallIfValid();
            if (call == null)
                Toast.makeText(requireContext(), R.string.roomInputNotValid, Toast.LENGTH_LONG).show();
            else {
                progressLayout.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Room>() {
                    @Override
                    public void onResponse(@NonNull Call<Room> call, @NonNull Response<Room> response) {
                        progressLayout.setVisibility(View.INVISIBLE);

                        if(!response.isSuccessful())
                        {
                            Toast.makeText(requireContext(),"Room hasn´t been created", Toast.LENGTH_SHORT).show();
                            try {
                                Log.e(TAG,response.errorBody().string());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            return;
                        }

                        Log.i(TAG, "Successfully posted room to server.\nResponseBody=\n" + response.body());

                        if (args == null)
                            Toast.makeText(requireContext(), R.string.roomCreateSuccess, Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(requireContext(), R.string.roomUpdateSuccess, Toast.LENGTH_SHORT).show();

                        roomViewModel.addRoom(response.body());
                        NavHostFragment.findNavController(RoomDetailFragment.this).navigateUp();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Room> call, @NonNull Throwable t) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Failed to create room.", t);
                        Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        if (args != null) {
            final Button btnDelete = view.findViewById(R.id.deleteButton);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                final Call<Room> call = viewModel.deleteRoom();
                progressLayout.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Room>() {
                    @Override
                    public void onResponse(@NonNull Call<Room> call, @NonNull Response<Room> response) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "Successfully deleted room.\nResponseBody=\n" + response.body());
                        Toast.makeText(requireContext(), R.string.roomDeleteSuccess, Toast.LENGTH_SHORT).show();

                        final boolean isRemoved = roomViewModel.removeRoom(response.body());
                        if (!isRemoved)
                            roomViewModel.sync(null);
                        NavHostFragment.findNavController(RoomDetailFragment.this).navigateUp();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Room> call, @NonNull Throwable t) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Failed to delete room.", t);
                        Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
