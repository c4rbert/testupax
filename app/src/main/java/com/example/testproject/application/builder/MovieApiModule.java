package com.example.testproject.application.builder;

import com.example.testproject.api.MovieApi;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
public class MovieApiModule {

    @AppScope
    @Provides
    MovieApi movieApiClient(OkHttpClient client, GsonConverterFactory gson, RxJava2CallAdapterFactory rxAdapter){
        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(gson)
                .addCallAdapterFactory(rxAdapter)
                .build();
        return retrofit.create(MovieApi.class);
    }
}