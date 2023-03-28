/** Author: University of Alberta: CMPUT 301 - Winter 2023
 *  Editor(s): Derek
 *  Purpose: View the list of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: ListyCity project / Lab 5
 */

/*
//Get the current activity
final Activity activity = getActivity();
activityMain = (MainActivity)activity;

//Find the layout
View root = inflater.inflate(R.layout.code_listview_layout, container, false);

//Get a reference to the ListView and create an object for the city list
codeList = root.findViewById(R.id.code_list);
scannableCodeDataList = new ArrayList<>();

// Set the adapter for the ListView to the CustomAdapter that
scannableCodeAdapter = new CustomList(this.getContext(), scannableCodeDataList, activityMain);
codeList.setAdapter(scannableCodeAdapter);

loadCodes();

return root;
 */

package com.example.qrcity.qr;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.qrcity.R;
import com.example.qrcity.TestObjects;
import com.example.qrcity.User;

import java.util.ArrayList;

public class CodeListview extends AppCompatActivity implements CustomList.CodeListListener {

    // Declare the variables so that you will be able to reference it later.
    private String ThisUserID;
    private String TargetUserID;
    boolean sameUser;
    private DataBase dataBase;
    private User user;

    private ListView codeList;
    private ArrayAdapter<ScannableCode> scannableCodeAdapter;
    private ArrayList<ScannableCode> scannableCodeDataList;
    private MainActivity activityMain;

    /** --- Remove this --- **/
    private TestObjects objs = new TestObjects();
    private ArrayList<ScannableCode> ExternalCodeDataList = objs.mockScannableCodes();
    /** ------------------- **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ThisUserID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Bundle bundle = getIntent().getExtras();
        TargetUserID = bundle.getString("userID");
        sameUser = ThisUserID.compareTo(TargetUserID) == 0;

        dataBase = new DataBase();
        user = dataBase.getUserById(TargetUserID);

        setContentView(R.layout.code_listview);

        //Get a reference to the ListView and create an object for the city list
        codeList = findViewById(R.id.code_list);
        scannableCodeDataList = new ArrayList<>();

        // Set the adapter for the ListView to the CustomAdapter
        scannableCodeAdapter = new CustomList(this, scannableCodeDataList, sameUser);
        codeList.setAdapter(scannableCodeAdapter);

        loadCodes();
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

    public void removeCode(String codeID){
        //TODO: user.removeCode(String codeID);
        //TODO: dataBase.removeCode(String codeID);

        /** --- Remove this --- **/
        for (int i = 0; i < ExternalCodeDataList.size(); i++) {
            if (ExternalCodeDataList.get(i).getId() == codeID){
                ExternalCodeDataList.remove(i);
                break;
            }
        }
        /** ------------------- **/

        loadCodes();
    }
}