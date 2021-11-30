package com.example.testproject.screen.main.dagger;

import com.example.testproject.application.builder.AppComponent;
import com.example.testproject.screen.main.MainActivity;

import dagger.Component;

@MainScope
@Component(modules = {MainModule.class}, dependencies = {AppComponent.class})
public interface MainComponent {
    void inject(MainActivity activity);
}
