/** Author: University of Alberta: CMPUT 301 - Winter 2023
 *  Editor(s): Derek
 *  Purpose: View the list of ScannableCode's
 *
 *  References:
 *      1) University of Alberta: CMPUT 301 - Winter 2023: ListyCity project / Lab 5
 */

package com.example.qrcity;

import static android.content.pm.PackageManager.PERMISSION_DENIED;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.budiyev.android.codescanner.CodeScannerView;
import com.example.qrcity.databinding.CodeListviewLayoutBinding;
import com.example.qrcity.databinding.FragmentCodeScannerBinding;

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
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void loadCodes(){
        // Clear the old list
        scannableCodeDataList.clear();

        //Get the list of codes from activityMain
        scannableCodeDataList = activityMain.getCodeList();

        //Notifying the adapter to render any new data fetched from the cloud
        scannableCodeAdapter.notifyDataSetChanged();
    }

    //TODO: remove code from database in activityMain
    public void removeCode(String codeID){
        //TODO: remove code from database in activityMain

        loadCodes();
    }
}
