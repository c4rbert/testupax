package com.example.testproject.util;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.testproject.model.ItemGeoData;
import com.example.testproject.service.SaveLocation;
import com.google.android.gms.location.FusedLocationProviderClient;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;


public class GeoDataLocation {

    private String TAG = this.getClass().getSimpleName();
    private FusedLocationProviderClient client;
    private SaveLocation context;
    private ItemGeoData itemGeoData;

    public GeoDataLocation(SaveLocation context) {
        this.context = context;
        client = LocationServices.getFusedLocationProviderClient(context);
    }

    @SuppressLint("MissingPermission")
    public void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            try {
                client.getLastLocation()
                        .addOnCompleteListener(new OnCompleteListener<Location>() {
                            @Override
                            public void onComplete(@NonNull Task<Location> task) {
                                if (task.isSuccessful() && task.getResult() != null) {
                                    itemGeoData = new ItemGeoData(task.getResult().getLatitude(), task.getResult().getLongitude());
                                } else {
                                    Log.w(TAG, "Failed to get location.");
                                }
                            }
                        });
            } catch (SecurityException unlikely) {
                Log.e(TAG, "Lost location permission." + unlikely);
            }

        } else {
            context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    private void setLocationData(Location locationData) {
        itemGeoData = new ItemGeoData(locationData.getLongitude(), locationData.getLatitude());
    }

    public ItemGeoData getItemGeoData() {
        return itemGeoData;
    }
}
