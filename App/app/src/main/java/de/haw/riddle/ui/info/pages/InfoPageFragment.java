package de.haw.riddle.ui.info.pages;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.fragment.NavHostFragment;

import de.haw.riddle.MainActivity;
import de.haw.riddle.R;


public class InfoPageFragment extends Fragment {
    private static final String INDEX = "index";

    private InfoPageViewModel viewModel;

    public static InfoPageFragment newInstance(int index) {
        Bundle args = new Bundle();
        args.putInt(INDEX, index);

        InfoPageFragment fragment = new InfoPageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(InfoPageViewModel.class);
        viewModel.setIndex(requireArguments().getInt(INDEX));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_info_page_general, container, false);

        ViewStub viewStub = view.findViewById(R.id.stub);
        viewStub.setLayoutResource(R.layout.fragment_info_page_general);
        switch (requireArguments().getInt(INDEX)){
            case 0:
                viewStub.setLayoutResource(R.layout.fragment_info_page_1);
                break;
            case 1:
                viewStub.setLayoutResource(R.layout.fragment_info_page_2);
                break;
            case 2:
                viewStub.setLayoutResource(R.layout.fragment_info_page_3);
                break;
        }

        viewStub.inflate();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       // TextView text = view.findViewById(R.id.text);
       // viewModel.getText().observe(getViewLifecycleOwner(), text::setText);
        if(requireArguments().getInt(INDEX) == 2) {
            final Button btnStart = view.findViewById(R.id.btnStart);
            btnStart.setVisibility(View.VISIBLE);
            btnStart.setOnClickListener(v -> {
                NavHostFragment.findNavController(this).navigate(R.id.action_fragment_info_to_fragment_overview);
                ((MainActivity) requireActivity()).showDrawerAndMenu();
            });
        }
    }
}
