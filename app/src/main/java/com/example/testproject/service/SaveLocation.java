package com.example.testproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.testproject.dao.DAOUser;
import com.example.testproject.model.ItemGeoData;
import com.example.testproject.model.User;
import com.example.testproject.util.GeoDataLocation;

public class SaveLocation extends Service {

    private String TAG = this.getClass().getSimpleName();

    private Handler handler;
    private Runnable r;

    private DAOUser daoUser;

    private GeoDataLocation geoDataLocation;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "Service created...");
        daoUser = new DAOUser();
        geoDataLocation = new GeoDataLocation(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started...");
        saveLocation();
        handler = new Handler(Looper.getMainLooper());
        r = () -> saveLocation();
        startHandler();
        return START_NOT_STICKY;
    }

    private void saveLocation() {
        geoDataLocation.getCurrentLocation();
        new Thread(() -> {
            ItemGeoData itemGeoData = geoDataLocation.getItemGeoData();
            while (itemGeoData == null) {
                itemGeoData = geoDataLocation.getItemGeoData();
            }
            User user = new User("Test", itemGeoData.getLatitude(), itemGeoData.getLongitude());
            daoUser.add(user).addOnSuccessListener(suc -> {
                Log.d(TAG, "Location inserted to firebase");
            });
        }).start();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "Service destroyed...");
        stopHandler();
    }

    //Every 30 seconds the service will save new coordinates
    public void startHandler() {
        handler.postDelayed(r, 30 * 60 * 1000);
    }

    public void stopHandler() {
        handler.removeCallbacks(r);
    }
}
