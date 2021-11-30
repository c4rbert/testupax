package com.example.testproject.application;

import android.app.Application;

import androidx.appcompat.app.AppCompatActivity;

import com.example.testproject.application.builder.AppComponent;
import com.example.testproject.application.builder.AppContextModule;
import com.example.testproject.application.builder.DaggerAppComponent;
import com.example.testproject.application.builder.NetworkModule;

public class AppController extends Application {

    public static AppController instance;
    private static AppComponent appComponent;

    @Override
    public void onCreate(){
        super.onCreate();
        instance = this;
        initAppComponent();
    }


    public static AppComponent getAppComponent() {
        return appComponent;
    }

    private void initAppComponent() {
        appComponent = DaggerAppComponent
                .builder()
                .appContextModule(new AppContextModule(this))
                .networkModule(new NetworkModule(this))
                .build();
    }

    public static AppCompatActivity getAppcompact(){
        return getAppcompact();
    }
}
