package com.example.testproject.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.testproject.model.Result;

import java.util.List;

@Dao
public interface MoviesDao {

    @Query("SELECT * FROM result")
    List<Result> getMovies();

    @Insert
    void insertMovie(Result... result);

    @Query("DELETE FROM result")
    void deleteAll();
}
