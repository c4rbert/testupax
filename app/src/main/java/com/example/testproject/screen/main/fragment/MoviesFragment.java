package com.example.testproject.screen.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testproject.R;
import com.example.testproject.databinding.FragmentMoviesBinding;
import com.example.testproject.model.MovieListResponse;
import com.example.testproject.model.Result;
import com.example.testproject.screen.main.adapter.MoviesAdapter;
import com.example.testproject.screen.main.core.MainView;

import java.io.Serializable;
import java.util.List;

public class MoviesFragment extends Fragment {

    private FragmentMoviesBinding binding;
    private MainView view;

    private MoviesAdapter moviesAdapter;
    private List<Result> moviesList;


    public static MoviesFragment newInstance(MainView view) {
        MoviesFragment fragment = new MoviesFragment();
        fragment.view = view;
        return fragment;
    }

    public MoviesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMoviesBinding.inflate(inflater, container, false);
        initRecyclerView();
        return binding.getRoot();
    }

    private void initRecyclerView() {
        moviesAdapter = new MoviesAdapter(moviesList);
        binding.moviesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.moviesRecyclerView.setAdapter(moviesAdapter);
        moviesAdapter.notifyDataSetChanged();
    }

    public void setMovieListResponse(List<Result> movieListResponse) {
        this.moviesList = movieListResponse;
    }
}