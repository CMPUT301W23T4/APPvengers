/** Author: University of Alberta: CMPUT 301 - Winter 2023
 *  Editor(s): Derek
 *  Purpose: Model the list view of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: ListyCity project / Lab 5
 */

package com.example.qrcity.qr;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.qrcity.R;

import java.util.ArrayList;

public class CustomList extends ArrayAdapter<ScannableCode> {

    private ArrayList<ScannableCode> codes;
    private Context context;
    boolean sameUser;
    Button removeCodeButton;

    interface CodeListListener{
        void removeCode(String codeID);
    }

    private CodeListListener listener;

    public CustomList(Context context, ArrayList<ScannableCode> codes, boolean sameUser){
        super(context,0, codes);
        this.codes = codes;
        this.context = context;
        this.sameUser = sameUser;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if(view == null){
            if (sameUser){
                view = LayoutInflater.from(context).inflate(R.layout.code_content_1, parent,false);
            } else{
                view = LayoutInflater.from(context).inflate(R.layout.code_content_2, parent,false);
            }
        }

        ScannableCode code = codes.get(position);

        ImageView image = view.findViewById(R.id.location_image);
        TextView codeName = view.findViewById(R.id.name);
        TextView codeScore = view.findViewById(R.id.score);
        TextView codeComment = view.findViewById(R.id.comment);

        codeName.setText(code.getName());
        codeScore.setText(Integer.toString(code.getScore()));
        codeComment.setText(code.getComment());
        image.setImageBitmap(code.getPhoto());


        if (context instanceof CodeListListener) {
            listener = (CodeListListener) context;
        } else {
            throw new RuntimeException(context + " must implement CodeListListener");
        }

        if (sameUser){
            //Get the button
            removeCodeButton = view.findViewById(R.id.button_delete_code);

            //On button Click
            removeCodeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.removeCode(code.getId());
                }
            });
        }


        return view;

    }
}
