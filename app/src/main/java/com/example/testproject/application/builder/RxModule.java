package com.example.testproject.application.builder;

import com.example.testproject.rx.AppRxSchedulers;
import com.example.testproject.rx.RxSchedulers;

import dagger.Module;
import dagger.Provides;

@Module
public class RxModule {

    @Provides
    RxSchedulers provideRxSchedulers() {
        return new AppRxSchedulers();
    }
}