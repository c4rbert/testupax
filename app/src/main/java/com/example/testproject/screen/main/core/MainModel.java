package com.example.testproject.screen.main.core;

import com.example.testproject.api.MovieApi;
import com.example.testproject.dao.DAOUser;
import com.example.testproject.model.MovieListResponse;

import io.reactivex.Observable;

public class MainModel {

    private MovieApi movieApi;

    private DAOUser daoUser;

    public MainModel(MovieApi movieApi){
        daoUser = new DAOUser();
        this.movieApi = movieApi;
    }

    public Observable<MovieListResponse> searchMovie(String apiKey, String query){
        return movieApi.searchMovie(apiKey, query);
    }
}
