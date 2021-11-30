package com.example.testproject.api;

import com.example.testproject.model.MovieListResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieApi {

    @GET("search/movie/")
    Observable<MovieListResponse> searchMovie(@Query("api_key") String apiKey, @Query("query") String query);

}
