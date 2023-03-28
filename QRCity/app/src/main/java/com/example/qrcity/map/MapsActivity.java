package com.example.qrcity.map;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.qrcity.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {
    public GoogleMap gMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 9802;
    private LatLng deviceLocation;
    private boolean LocationPermissionGranted = false;

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Log.d(TAG, "Map is ready!");
        gMap = googleMap;

        if (LocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            gMap.setMyLocationEnabled(true);
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mapview);

        Intent intent = getIntent();

        getLocationPermission();
        initMap();
    }

    /**
     * initialize map
     */
    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MapsActivity.this);
    }
    private void getLocationPermission() {
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationPermissionGranted = true;
            }else{
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            return;
                        }
                    }
                    LocationPermissionGranted = true;
                }
            }
        }
    }
    private void getDeviceLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if(LocationPermissionGranted){
                Task location = fusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation != null){
                                deviceLocation = new LatLng(currentLocation.getLatitude(),currentLocation.getLongitude());
                                addMark(deviceLocation,15f);
                            }
                            else {
                                getDeviceLocation();
                            }
                        }
                    }
                });
            }else {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "No Permission",
                        Toast.LENGTH_SHORT);

                toast.show();
            }
        }catch (SecurityException securityException){
            Log.d(TAG,"getDeviceLocation: SecurityException: " + securityException.getMessage());
        }
    }
    private void addMark(LatLng latLng, float zoom){ List<List<Double>> codeList = new LinkedList<>();
        if(LocationPermissionGranted){
            Log.d(TAG,"Current location is latitude: " + latLng.latitude + ", longitude: " + latLng.longitude);
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            CollectionReference collectionReference = db.collection("ScannableCodes");
            collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException error) {
                    assert queryDocumentSnapshots != null;
                    for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                        List<Double> codeLocation;
                        Map<String,Object> code = doc.getData();
                        for(Map.Entry<String, Object> pair : code.entrySet()) {
                            String key = pair.getKey();
                            if (pair.getKey().equals("Location")) {
                                codeLocation = (List<Double>) pair.getValue();
                                codeList.add(codeLocation);
                                if(codeLocation.get(0) != 0 && codeLocation.get(0) != 0) {
                                    gMap.addMarker(new MarkerOptions().position(new LatLng(codeLocation.get(0), codeLocation.get(1))).title("[" + codeLocation.get(0) + "," + codeLocation.get(1) + "]"));
                                    Log.d(TAG, "*****Added code marker to: latitude: " + codeLocation.get(0) + ", longitude: " + codeLocation.get(1));
                                }
                            }
                        }
                    }
                }
            });
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(deviceLocation,15f));
        }

    }








}