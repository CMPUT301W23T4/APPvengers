package com.example.qrcity.qr;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.qrcity.R;
import com.example.qrcity.user.TestObjects;
import com.example.qrcity.user.User;
import com.example.qrcity.user.UserCallback;

import java.util.ArrayList;

public class UserStatisticsView extends AppCompatActivity implements CustomList.CodeListListener{
    // Declare the variables so that you will be able to reference it later.
    private String ThisUserID;
    private String TargetUserID;
    boolean sameUser;
    private DataBase dataBase;
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

    /** --- Remove this --- **/
    private TestObjects objs = new TestObjects();
    private ArrayList<ScannableCode> ExternalCodeDataList = objs.mockScannableCodes();
    /** ------------------- **/

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
        dataBase = DataBase.getInstance();
        dataBase.getUserById(TargetUserID, new UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                current_user = user;
            }

            @Override
            public void onUserRetrievalError(Exception e) {
                Log.i(TAG, "----- Could not get user -----");
            }
        });

        if (current_user.getUserCodeList().size() != 0)
        {
            setContentView(R.layout.user_statistics);
        }

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

    public void refresh(){
        loadCodes();

        ScannableCode max = scannableCodeDataList.get(0);
        ScannableCode min = scannableCodeDataList.get(0);

        sum = 0;
        n = 0;

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

        sumValue.setText(Integer.toString(sum));
        nValue.setText(Integer.toString(n));
    }

    public void loadCodes(){
        // Clear the old list
        scannableCodeDataList.clear();

        /**Get the list of codes from the user
         for (Map codeID: user.getUserCodeList()) {
         scannableCodeDataList.add(dataBase.getCode((String) codeID.get("id")));
         }
         */

        /** --- Remove this --- **/
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
