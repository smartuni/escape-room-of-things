package de.haw.riddle.ui.admin.riddle;

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
import de.haw.riddle.ui.admin.model.Riddle;
import de.haw.riddle.util.SimpleTextWatcher;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RiddleDetailFragment extends DaggerFragment {

    public static final String TAG = RiddleDetailViewModel.class.getSimpleName();
    private static final String KEY_RIDDLE = "riddle";
    private static final String KEY_PARENT_ROOM_ID ="roomId" ;

    private View progressLayout;

    @Inject
    RiddleViewModel riddleViewModel;
    @Inject
    RiddleDetailViewModel viewModel;

    public static Bundle createArgs(@Nullable Riddle riddle, long roomId) {
        Bundle args = new Bundle();
        args.putLong(KEY_PARENT_ROOM_ID,roomId);
        args.putParcelable(KEY_RIDDLE, riddle);
        return args;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_riddle_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextInputEditText tfName = view.findViewById(R.id.tfName);
        TextInputEditText tfDescription = view.findViewById(R.id.tfDescription);
        TextInputEditText tfState = view.findViewById(R.id.tfState);
        TextInputEditText tfRoom = view.findViewById(R.id.tfRoom);
        progressLayout = view.findViewById(R.id.progressLayout);
        progressLayout.setOnTouchListener((v, event) -> {
            v.performClick();
            return true;
        });

        Bundle args = getArguments();
        if (args != null) {
            final Riddle riddle= args.getParcelable(KEY_RIDDLE);
            if(riddle!=null){
                view.findViewById(R.id.tlId).setVisibility(View.VISIBLE);


                viewModel.setData(riddle,riddle.getRoom());
                view.findViewById(R.id.tlId).setVisibility(View.VISIBLE);
                ((TextInputEditText) view.findViewById(R.id.tfId)).setText(String.valueOf(riddle.getId()));
                tfName.setText(riddle.getName());
                tfDescription.setText(riddle.getDescription());
                tfState.setText(riddle.getState());
                tfRoom.setText((riddle.getRoom()));

                view.findViewById(R.id.applyButton).setOnClickListener(v -> {
                    Toast.makeText(requireContext(), R.string.roomUpdateSuccess, Toast.LENGTH_SHORT).show();
                    viewModel.updateRiddle(tfRoom.getText().toString()).enqueue(new Callback<Riddle>() {
                        @Override
                        public void onResponse(Call<Riddle> call, Response<Riddle> response) {
                            progressLayout.setVisibility(View.INVISIBLE);
                            Log.i(TAG, "ResponseCode= "+response.code());
                            if(!response.isSuccessful())
                            {
                                Toast.makeText(requireContext(),"Riddle hasn´t been updatet", Toast.LENGTH_SHORT).show();
                                try {
                                    Log.e(TAG,response.errorBody().string());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }

                                return;
                            }
                        }

                        @Override
                        public void onFailure(Call<Riddle> call, Throwable t) {

                        }
                    });

                });


            }
            else {
                final long parentRoomId=args.getLong(KEY_PARENT_ROOM_ID);
                tfRoom.setText(String.valueOf(parentRoomId));
                viewModel.setData(null,String.valueOf(parentRoomId));

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
                                Log.i(TAG, "ResponseCode= "+response.code());
                                if(!response.isSuccessful())
                                {
                                    Toast.makeText(requireContext(),"Riddle hasn´t been created", Toast.LENGTH_SHORT).show();
                                    try {
                                        Log.e(TAG,response.errorBody().string());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                    return;
                                }

                                Log.i(TAG, "Successfully posted riddle to server.\nResponseBody=\n" + response.body());
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

        tfName.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setName(s.toString());
            }
        });

        tfDescription.addTextChangedListener(new SimpleTextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setDescription(s.toString());
            }
        });



        if (args != null) {
            final Button btnDelete = view.findViewById(R.id.deleteButton);
            btnDelete.setVisibility(View.VISIBLE);
            btnDelete.setOnClickListener(v -> {
                final Call<Riddle> call = viewModel.deleteRiddle();
                progressLayout.setVisibility(View.VISIBLE);
                call.enqueue(new Callback<Riddle>() {
                    @Override
                    public void onResponse(@NonNull Call<Riddle> call, @NonNull Response<Riddle> response) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.i(TAG, "Successfully deleted riddle.\nResponseBody=\n" + response.body());
                        Toast.makeText(requireContext(), R.string.roomDeleteSuccess, Toast.LENGTH_SHORT).show();

                        final boolean isRemoved = riddleViewModel.removeRiddle(response.body());
                        if (!isRemoved)
                            riddleViewModel.sync(null);
                        NavHostFragment.findNavController(RiddleDetailFragment.this).navigateUp();
                    }

                    @Override
                    public void onFailure(@NonNull Call<Riddle> call, @NonNull Throwable t) {
                        progressLayout.setVisibility(View.INVISIBLE);
                        Log.e(TAG, "Failed to delete riddle.", t);
                        Toast.makeText(requireContext(), t.toString(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }

}
