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
import android.widget.TextView;

import com.example.qrcity.R;
import com.example.qrcity.user.TestObjects;
import com.example.qrcity.user.User;
import com.example.qrcity.user.UserCallback;

import java.util.ArrayList;

public class CodeListview extends AppCompatActivity implements CustomList.CodeListListener {

    // Declare the variables so that you will be able to reference it later.
    private String ThisUserID;
    private String TargetUserID;
    boolean sameUser;
    private DataBase dataBase;
    private User current_user;

    private ListView codeList;
    private ArrayAdapter<ScannableCode> scannableCodeAdapter;
    private ArrayList<ScannableCode> scannableCodeDataList;

    /** --- Remove this --- **/
    private TestObjects objs = new TestObjects();
    private ArrayList<ScannableCode> ExternalCodeDataList = objs.mockScannableCodes();
    /** ------------------- **/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Check if the target user is the same as the current user
        ThisUserID = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);
        Bundle bundle = getIntent().getExtras();
        TargetUserID = bundle.getString("userID");
        sameUser = ThisUserID.compareTo(TargetUserID) == 0;

        //Get a reference to the target user from the database
        dataBase = new DataBase();
        //TODO: load user from database
        /*
        dataBase.getUserById(TargetUserID, new UserCallback() {
            @Override
            public void onUserRetrieved(User user) {
                current_user = user;
            }

            @Override
            public void onUserRetrievalError(Exception e) {

            }
        });
         */
        current_user = objs.mockAdvancedUser();

        setContentView(R.layout.code_listview);

        //Set text at the top
        TextView listName = findViewById(R.id.list_name);
        listName.setText(current_user.getName()+"'s scanned codes");

        //Get a reference to the ListView and create an object for the list
        codeList = findViewById(R.id.code_list);
        scannableCodeDataList = new ArrayList<>();

        // Set the adapter for the ListView to the CustomAdapter
        scannableCodeAdapter = new CustomList(this, scannableCodeDataList, sameUser);
        codeList.setAdapter(scannableCodeAdapter);

        //Load statistics
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
