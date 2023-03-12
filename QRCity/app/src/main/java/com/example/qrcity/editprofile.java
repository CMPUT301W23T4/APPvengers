package com.example.qrcity;

import android.widget.EditText;

import androidx.fragment.app.DialogFragment;

public class editprofile extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private User user;
    private EditText name_edit;
    private EditText contact_edit;

    public interface OnFragmentInteractionListener {
        void onEditOKPressed(User user);
    }
}
