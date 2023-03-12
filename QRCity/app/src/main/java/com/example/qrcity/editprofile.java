package com.example.qrcity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

public class editprofile extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private User user;
    private EditText name_edit;
    private EditText contact_edit;

    public interface OnFragmentInteractionListener {
        void onEditOKPressed(User user);
    }
    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            listener = (OnFragmentInteractionListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString()
                    +"must implement OnFragmentInteractionListener");
        }
    }
    @Override
    @NonNull
    public Dialog onCreateDialog(@NonNull Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.edit_profile, null);
        name_edit = view.findViewById(R.id.UserName_edit);
        contact_edit = view.findViewById(R.id.content_edit);
        name_edit.setText(user.getName());
        contact_edit.setText(user.getContactInfo());
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setTitle("Edit profile")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.setName(name_edit.getText().toString());
                        user.setContactInfo(contact_edit.getText().toString());
                        listener.onEditOKPressed(user);
                    }
                }).create();
    }
    public editprofile(User user){
        this.user = user;
    }
}
