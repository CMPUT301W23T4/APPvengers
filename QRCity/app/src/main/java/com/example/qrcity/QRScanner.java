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

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //Current Activity
        final Activity activity = getActivity();

        //Find scanner from layout
        View root = inflater.inflate(R.layout.fragment_code_scanner, container, false);
        CodeScannerView scannerView = root.findViewById(R.id.scanner_view);

        //TODO: Get Camera Permissions
            
        //Create a new scanner
        mCodeScanner = new CodeScanner(activity, scannerView);
        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        try{
                            onSuccessfulScan(result);
                        }
                        catch (Exception exception){
                            Log.e(TAG, "Error - Could not hash result");
                        }
                    }
                });
            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });

        binding = FragmentCodeScannerBinding.inflate(inflater, container, false);
        return root;//binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.buttonSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(QRScanner.this)
                        .navigate(R.id.action_CodeScannerFragment_to_FirstFragment);
            }
        });
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

    /**
     * This returns the 64 hexadecimal hash of the input as a string
     * @param input
     * Code from reference (3)
     */
    public String getHash(String input) {
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
            System.out.println("SHA-256 algorithm not supported");
            return null;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void onSuccessfulScan(Result result){

        String hash = getHash(result.getText());

        //TODO: Go to different fragment and pass the hash to it

        NavHostFragment.findNavController(QRScanner.this)
                .navigate(R.id.action_FirstFragment_to_CodeScannerFragment);
    }
}