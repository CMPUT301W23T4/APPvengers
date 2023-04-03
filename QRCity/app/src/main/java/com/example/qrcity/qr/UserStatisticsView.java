package com.example.qrcity.qr;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcity.R;
import com.example.qrcity.user.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

public class UserStatisticsView extends AppCompatActivity implements CustomList.CodeListListener{
    // Declare the variables so that you will be able to reference it later.
    private String ThisUserID;
    private String TargetUserID;
    boolean sameUser;
    private DataBase dataBase = DataBase.getInstance();
    private User current_user;

    private ListView codeList;
    private ArrayAdapter<ScannableCode> scannableCodeAdapter;
    private ArrayList<ScannableCode> scannableCodeDataList;

    private int sum;
    private int n;

    private TextView sumValue;
    private TextView nValue;

    private Button viewCodeList;

    private Activity currentActivity;

    private FirebaseFirestore fbdb = FirebaseFirestore.getInstance();

    private final String TAG = "Text from UserStatisticsView: ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = this;

        //Check if the target user is the same as the current user
        ThisUserID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Bundle bundle = getIntent().getExtras();
        TargetUserID = bundle.getString("userID");
        sameUser = ThisUserID.compareTo(TargetUserID) == 0;

        //Get a reference to the target user from the database
        current_user = dataBase.getUserFromUserData(TargetUserID);

        setContentView(R.layout.user_statistics);

        //Set text at the top
        TextView statsName = findViewById(R.id.name);
        statsName.setText(current_user.getName()+"'s Stats");

        //Get a reference to the ListView and create an object for the list
        codeList = findViewById(R.id.HighLowCodeList);
        scannableCodeDataList = new ArrayList<>();
        TextView listName = findViewById(R.id.HighLowCodes);
        listName.setText(current_user.getName()+"'s Highest and Lowest scoring codes");


        // Set the adapter for the ListView to the CustomAdapter
        scannableCodeAdapter = new CustomList(this, scannableCodeDataList, false);
        codeList.setAdapter(scannableCodeAdapter);


        //Load statistics
        sumValue = findViewById(R.id.SumOfCodesValue);
        nValue = findViewById(R.id.NumCodesValue);

        refresh();

        viewCodeList = findViewById(R.id.ViewFullCodeListButton);
        viewCodeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("Clicked View Full Code List Button");

                //Pass forward the userID
                Bundle bundle = new Bundle();
                bundle.putString("userID", TargetUserID);
                Intent intent = new Intent(currentActivity, CodeListview.class);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the data to the bundle
        outState.putString("userID", TargetUserID);
    }

    public void refresh(){
        loadCodes();

        sum = 0;
        n = 0;

        if (current_user.getUserCodeList().size() > 0){
            ScannableCode max = scannableCodeDataList.get(0);
            ScannableCode min = scannableCodeDataList.get(0);

            for (ScannableCode code: scannableCodeDataList) {
                sum += code.getScore();
                n += 1;
                if (code.getScore() > max.getScore()){
                    max = code;
                }
                if (code.getScore() < min.getScore()){
                    min = code;
                }
            }

            scannableCodeDataList.clear();
            scannableCodeDataList.add(max);
            scannableCodeDataList.add(min);

            scannableCodeAdapter.notifyDataSetChanged();
        }

        sumValue.setText(Integer.toString(sum));
        nValue.setText(Integer.toString(n));
    }

    public void loadCodes(){
        // Clear the old list
        scannableCodeDataList.clear();

        //**Get the list of codes from the user
         for (Map codeID: current_user.getUserCodeList()) {
            scannableCodeDataList.add(dataBase.getCodeFromCodeData((String) codeID.get("id")));
         }
         //*/

        /** --- Remove this --- /
        for (ScannableCode code: ExternalCodeDataList) {
            scannableCodeDataList.add(code);
        }
        /** ------------------- **/

        //Notifying the adapter to render any new data fetched from the cloud
        scannableCodeAdapter.notifyDataSetChanged();
    }

    public void removeCode(String codeID){}

    @Override
    public void onResume(){
        super.onResume();
        refresh();
    }
}
