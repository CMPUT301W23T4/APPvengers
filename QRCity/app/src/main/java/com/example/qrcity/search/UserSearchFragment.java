package com.example.qrcity.search;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.qrcity.R;

public class UserSearchFragment extends DialogFragment {
    private OnFragmentInteractionListener listener;
    private String userName;
    private long score;
    private long Numcodes;

    public interface OnFragmentInteractionListener {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof UserSearchFragment.OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + "must implement OnFragmentInteractionListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.usersearchfragment, null);
        TextView nameText = view.findViewById(R.id.name);
        TextView scoreText = view.findViewById(R.id.totalscore);
        TextView codeText = view.findViewById(R.id.numcode);
        nameText.setText("User");
        scoreText.setText(String.valueOf("Score: " + score));
        codeText.setText(String.valueOf("Codes: " + Numcodes));
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder
                .setView(view)
                .setPositiveButton("OK", null).create();

    }
    public UserSearchFragment(String userName, long score, long Numcodes){
        this.userName = userName;
        this.score = score;
        this.Numcodes = Numcodes;
    }
}
