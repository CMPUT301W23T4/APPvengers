package com.example.qrcity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import androidx.core.app.ActivityCompat;
public class GPS_location {
    private LocationManager locationManager;
    public double[] deviceLocation = {0, 0};

    public GPS_location(Context context) {
        String serviceString = context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) context.getSystemService(serviceString);

        String provider = LocationManager.GPS_PROVIDER;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        }
        Location location = locationManager.getLastKnownLocation(provider);
        if (location != null){
            deviceLocation[0] = location.getLatitude();
            deviceLocation[1] = location.getLongitude();
        }
        else {
            deviceLocation[0] = 0;
            deviceLocation[1] = 0;
        }
    }

    public double[] getDeviceLocation() {
        return deviceLocation;
    }
}

