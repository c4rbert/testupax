package com.example.testproject.screen.main.core;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.testproject.dao.DAOPictures;
import com.example.testproject.dao.DAOUser;
import com.example.testproject.db.AppDatabase;
import com.example.testproject.model.MovieListResponse;
import com.example.testproject.model.PictureBase64;
import com.example.testproject.model.Result;
import com.example.testproject.rx.RxSchedulers;
import com.google.firebase.database.DataSnapshot;

import java.util.List;

import io.reactivex.observers.DisposableObserver;

public class MainPresenter {

    private String TAG = this.getClass().getSimpleName();
    private MainView view;
    private MainModel model;
    private DAOUser daoUser;
    private DAOPictures daoPictures;
    private RxSchedulers rxSchedulers;
    private DataSnapshot dataSnapshot;

    public MainPresenter(MainView view, MainModel model, RxSchedulers rxSchedulers) {
        this.view = view;
        this.view.setPresenter(this);
        this.model = model;
        this.rxSchedulers = rxSchedulers;
        daoUser = new DAOUser();
        daoPictures = new DAOPictures();
        getLocationData();
    }

    public void searchMovie(String query) {
        model.searchMovie("b3ae6a03cbf1dbc0f4cdbfe08e5b37ec", query)
                .subscribeOn(rxSchedulers.io())
                .observeOn(rxSchedulers.androidThread())
                .subscribeWith(new DisposableObserver<MovieListResponse>() {
                    @Override
                    public void onNext(@NonNull MovieListResponse movieListResponse) {
                        if (movieListResponse != null && movieListResponse.getResults().size() > 0) {
                            saveMovies(movieListResponse.getResults());
                            view.initMoviesFragment(movieListResponse.getResults(), query);
                        }else{
                            view.showError();
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        AppDatabase db = AppDatabase.getDbInstance(view.getContext());
                        List<Result> results = db.moviesDao().getMovies();
                        if (results != null && results.size() > 0) {
                            view.showLastSearch();
                            view.initMoviesFragment(results, view.witOutConnection());
                        }
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    private void saveMovies(List<Result> results) {
        AppDatabase db = AppDatabase.getDbInstance(view.getContext());
        db.moviesDao().deleteAll();
        for (Result result : results)
            db.moviesDao().insertMovie(result);
    }

    public void getLocationData() {
        daoUser.getLocationData(this);
    }

    public void setLocationData(DataSnapshot snapshot) {
        this.dataSnapshot = snapshot;
    }

    public DataSnapshot getDataSnapshot() {
        return dataSnapshot;
    }

    public void savePictures(List<PictureBase64> pictureBase64List) {
        daoPictures.add(pictureBase64List).addOnSuccessListener(suc -> {
            Log.d(TAG, "Images inserted");
            view.showSavedPictures();
        }).addOnFailureListener(e -> {
            view.showSavedPictures();
        });
    }
}
