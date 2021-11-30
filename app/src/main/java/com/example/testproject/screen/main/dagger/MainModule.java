package com.example.testproject.screen.main.dagger;

import com.example.testproject.api.MovieApi;
import com.example.testproject.rx.RxSchedulers;
import com.example.testproject.screen.main.MainActivity;
import com.example.testproject.screen.main.core.MainModel;
import com.example.testproject.screen.main.core.MainPresenter;
import com.example.testproject.screen.main.core.MainView;

import dagger.Module;
import dagger.Provides;

@Module
public class MainModule {

    private MainActivity context;

    public MainModule(MainActivity context) {
        this.context = context;
    }

    @Provides
    @MainScope
    public MainActivity provideContext() {
        return this.context;
    }

    @Provides
    @MainScope
    public MainView providesView(MainActivity context) {
        return new MainView(context);
    }

    @Provides
    @MainScope
    public MainPresenter providePresenter(MainView view, MainModel model, RxSchedulers rxSchedulers) {
        return new MainPresenter(view, model, rxSchedulers);
    }

    @Provides
    @MainScope
    public MainModel provideModel(MovieApi movieApi) {
        return new MainModel(movieApi);
    }
}
