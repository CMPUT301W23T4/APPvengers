package com.example.qrcity.qr;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcity.map.GPS_location;
import com.example.qrcity.R;
import com.example.qrcity.user.User;
import com.google.android.material.textfield.TextInputEditText;


public class Scannable_code_fragment extends Fragment {


    public static final String EXTRA_INFO = "default";
    private Button btnCapture;
    private Switch sw;
    private Button next_frag;
    private TextView score_textview;
    private TextInputEditText cmt_edit;
    private double[] location={0,0};
    private int score;
    private Bitmap photo=null;
    private ScoringSystem score_sys = new ScoringSystem();

    DataBase database = DataBase.getInstance();


    private static final int Image_Capture_Code = 1;
    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.scannable_code_fragment, container, false);
        btnCapture =(Button) view.findViewById(R.id.taking_pic_button);
        btnCapture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //access to camera to take photo
                Intent cInt = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cInt,Image_Capture_Code);
            }
        });
        cmt_edit=(TextInputEditText) view.findViewById(R.id.input_cmt);
        score_textview=(TextView) view.findViewById(R.id.score_view);
        show_score();
        return view;
    }
    private void show_score(){
        score=score_sys.getScore(((MainActivity)getActivity()).getLastHash()); //save score
        score_textview.setText("Code is worth: "+score);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //after capturing image by camera
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Image_Capture_Code) {
            if (resultCode == -1) {
                photo = (Bitmap) data.getExtras().get("data"); //save photo
                btnCapture.setText("Retake");
            } else if (resultCode == 0) {
                Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            }
        }
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //location switch
        sw=view.getRootView().findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                //turn on the switch to record the location
                if (b){
                    Toast.makeText(getContext(), "Location recorded!",
                        Toast.LENGTH_LONG).show();
                    GPS_location gps=new GPS_location(getContext());
                    location=gps.getDeviceLocation();               //save location
                }
                //turn off to not record the location
                else{
                    location[0]=0;
                    location[1]=0;
                }
            }
        });
        //Next button
        next_frag=view.getRootView().findViewById(R.id.next_button);
        next_frag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (photo==null){
                    Toast.makeText(getActivity(), "Please add the photo of the location!", Toast.LENGTH_LONG).show();
                }
                else{
                    save_data();
                    NavHostFragment.findNavController(Scannable_code_fragment.this)
                            .navigate(R.id.action_scannable_code_to_success_scan_fragment);
                }
            }
        });
    }

    private void save_data(){
        //create a scanned QR code
        String unique_id=((MainActivity)getActivity()).getLastHash()+((MainActivity)getActivity()).user_id;
        ScannableCode code=new ScannableCode(unique_id,score,cmt_edit.getText().toString(),location,photo,score_sys.getName(((MainActivity)getActivity()).getLastHash()));

        //add a code into QR code list.
        //((MainActivity)getActivity()).addCode(code);

        //TODO: load current user from database
        ////////////////////////////////////////////////////
        database.addCode(code);
        User user = database.getUserFromUserData(database.getThisUserID());
        user.addCode(code);
        database.editUser(user);
        database.loadAllUserData();
        database.loadAllCodeData();
    }

    public void setPhoto(Bitmap bitmap){
        photo = bitmap;
    }
}