package com.example.qrcity.qr;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.example.qrcity.R;

/**
 Cite: Stack overflow. (2019). Retrieved from https://stackoverflow.com/questions/54275594/how-to-start-camera-from-fragment-in-android
 */
public class success_scan_fragment extends Fragment {

    private Button next_frag;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.sucess_scan_fragment, container, false);



        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Next button
        next_frag=view.getRootView().findViewById(R.id.back_button);
        next_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(success_scan_fragment.this)
                        .navigate(R.id.action_success_scan_fragment_to_FirstFragment);
            }
        });
    }

}