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

package com.example.qrcity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.provider.Settings;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Map;

public class ThisUserListViewActivity extends AppCompatActivity implements CustomList.CodeListListener {

    // Declare the variables so that you will be able to reference it later.
    private String user_id;
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

         user_id = Settings.Secure.getString(this.getContentResolver(),Settings.Secure.ANDROID_ID);;
         dataBase = new DataBase();
         user = dataBase.getUserById(user_id);

        setContentView(R.layout.code_listview_layout);

        //Get a reference to the ListView and create an object for the city list
        codeList = findViewById(R.id.code_list);
        scannableCodeDataList = new ArrayList<>();

        // Set the adapter for the ListView to the CustomAdapter
        scannableCodeAdapter = new CustomList(this, scannableCodeDataList, activityMain);
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
