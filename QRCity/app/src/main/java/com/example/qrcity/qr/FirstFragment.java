package com.example.qrcity.qr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.qrcity.R;
import com.example.qrcity.databinding.FragmentFirstBinding;
import com.example.qrcity.map.MapsActivity;

public class FirstFragment extends Fragment {

    private FragmentFirstBinding binding;
    private MainActivity activityMain;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final Activity activity = getActivity();
        activityMain = (MainActivity)activity;

        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_CodeScannerFragment);
            }
        });


        binding.getRoot().findViewById(R.id.Records).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked records");

                //Pass forward the userID
                Bundle bundle = new Bundle();
                bundle.putString("userID", activityMain.getUser_id());
                Intent intent = new Intent(getContext(), CodeListview.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });

        binding.getRoot().findViewById(R.id.Rankings).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(FirstFragment.this)
                        .navigate(R.id.action_FirstFragment_to_leaderboardFragment);
            }
        });

        binding.getRoot().findViewById(R.id.Location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("Clicked location");
                Intent intent = new Intent(getContext(), MapsActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}