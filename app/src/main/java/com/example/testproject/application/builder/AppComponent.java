package com.example.testproject.application.builder;

import com.example.testproject.api.MovieApi;
import com.example.testproject.rx.RxSchedulers;

import dagger.Component;

@AppScope
@Component(modules = {AppContextModule.class, NetworkModule.class, RxModule.class, MovieApiModule.class})
public interface AppComponent {
    RxSchedulers rxSchedulers();
    MovieApi movieApiClient();
}
