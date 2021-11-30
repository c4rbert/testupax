package com.example.testproject.screen.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import com.example.testproject.application.AppController;
import com.example.testproject.screen.main.dagger.DaggerMainComponent;
import com.example.testproject.screen.main.dagger.MainModule;
import com.example.testproject.screen.main.core.MainModel;
import com.example.testproject.screen.main.core.MainPresenter;
import com.example.testproject.screen.main.core.MainView;
import com.example.testproject.service.SaveLocation;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {

    @Inject
    MainView view;
    @Inject
    MainPresenter presenter;
    @Inject
    MainModel model;

    private final int LOCATION_PERMISSION_CODE = 100;
    private Intent intentService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        DaggerMainComponent
                .builder()
                .appComponent(AppController.getAppComponent())
                .mainModule(new MainModule(this))
                .build()
                .inject(this);
        setContentView(view.getView());

        intentService = new Intent(this, SaveLocation.class);

        initLocation();
    }

    public void initLocation() {
        if (validatePermissions()) {
            startService(intentService);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            }
        }
    }

    private boolean validatePermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case LOCATION_PERMISSION_CODE:
                startService(intentService);
        }
    }
}