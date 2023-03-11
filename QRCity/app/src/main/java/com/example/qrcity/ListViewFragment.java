/** Author: University of Alberta: CMPUT 301 - Winter 2023
 *  Editor(s): Derek
 *  Purpose: View the list of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: ListyCity project / Lab 5
 */

package com.example.qrcity;

import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListViewFragment extends Fragment implements CustomList.CodeListListener {

    // Declare the variables so that you will be able to reference it later.
    ListView codeList;
    ArrayAdapter<ScannableCode> scannableCodeAdapter;
    ArrayList<ScannableCode> scannableCodeDataList;
    private MainActivity activityMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the current activity
        final Activity activity = getActivity();
        activityMain = (MainActivity)activity;


        View root = inflater.inflate(R.layout.code_listview_layout, container, false);

        //Get a reference to the ListView and create an object for the city list
        codeList = root.findViewById(R.id.code_list);
        scannableCodeDataList = new ArrayList<>();

        // Set the adapter for the ListView to the CustomAdapter that
        scannableCodeAdapter = new CustomList(this.getContext(), scannableCodeDataList, activityMain);
        codeList.setAdapter(scannableCodeAdapter);

        loadCodes();

        return root;
    }

    public void loadCodes(){
        // Clear the old list
        scannableCodeDataList.clear();

        //TODO: Get the list of codes from the database in activityMain

        //Notifying the adapter to render any new data fetched from the cloud
        scannableCodeAdapter.notifyDataSetChanged();
    }

    public void removeCode(String codeID){
        //TODO: remove code from database in activityMain based on ID
    }
}
