/** Author(s): Derek
 *  Purpose: Scan QR codes and Bar codes and return the hash value of those codes
 *
 *  References:
 *      1) youtube video: "QR & Barcode Scanner App Tutorial in Android Studio" by Indently
 *          -> Video Link: https://www.youtube.com/watch?v=drH63NpSWyk&ab_channel=Indently
 *          -> Youtube Channel Link: https://www.youtube.com/@Indently
 *
 *      2) code-scanner Dependency
 *          -> GitHub repository link: https://github.com/yuriy-budiyev/code-scanner
 *          -> Authors: Yuriy Budiyev et al.
 *          
 *      3) Article: "SHA-256 Hash in Java" by bilal-hungund hosted by geeks for geeks
 *          -> Author: @bilal-hungund
 *          -> Link: https://www.geeksforgeeks.org/sha-256-hash-in-java/
 */

package com.example.qrcity;

import static android.content.ContentValues.TAG;
import static android.content.pm.PackageManager.PERMISSION_DENIED;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.qrcity.databinding.FragmentCodeScannerBinding;
import com.google.zxing.Result;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class defines a Code Scanner
 */

public class QRScanner extends Fragment {

    private FragmentCodeScannerBinding binding;
    private CodeScanner mCodeScanner;
    private MainActivity activityMain;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Get the current activity
        final Activity activity = getActivity();
        activityMain = (MainActivity)activity;

        //Find scanner from layout
        View root = inflater.inflate(R.layout.fragment_code_scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);

        //Get Camera Permissions
        if (ContextCompat.checkSelfPermission(getContext() , Manifest.permission.CAMERA) == PERMISSION_DENIED){
            ActivityResultLauncher<String> requestPermissionLauncher =
                    registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                        if (isGranted) {
                            //If permission granted, create a new scanner
                            createScanner(activity, scannerView);
                        }
                    });

            requestPermissionLauncher.launch(Manifest.permission.CAMERA);
        } else{
            //If permission already granted, Create a new scanner
            createScanner(activity, scannerView);
        }

        //Display the camera preview on screen
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        binding = FragmentCodeScannerBinding.inflate(inflater, container, false);
        return root;

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void createScanner(Activity activity, CodeScannerView scannerView){
        //Create a new scanner
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //Once the scan is Successful
                        onSuccessfulScan(result);
                    }
                });
            }
        });
    }

    /**
     * This returns the 64 hexadecimal hash of the input as a string
     * @param input
     * Code from reference (3)
     */
    public String calculateHash(String input) {
        try {
            // Static getInstance method is called with hashing SHA
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            // digest() method called
            // to calculate message digest of an input
            // and return array of byte
            byte[] hash = md.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation
            BigInteger number = new BigInteger(1, hash);

            // Convert message digest into hex value
            StringBuilder hexString = new StringBuilder(number.toString(16));

            // Pad with leading zeros
            while (hexString.length() < 64)
            {
                hexString.insert(0, '0');
            }

            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            // Handle the case where SHA-256 is not a supported algorithm
            Log.e(TAG, "SHA-256 algorithm not supported");
            return null;
        }
    }

    private void onSuccessfulScan(Result result){

        String hash = calculateHash(result.getText());
        if (hash == null){
            return;
        } else{
            Log.i(TAG, "Hash calculated successfully");
            activityMain.setLastHash(hash);
            Log.i(TAG, "Hash stored successfully");
            Log.i(TAG, activityMain.getLastHash());
        }

        NavHostFragment.findNavController(QRScanner.this)
                .navigate(R.id.action_CodeScannerFragment_to_scannable_code);
    }
}