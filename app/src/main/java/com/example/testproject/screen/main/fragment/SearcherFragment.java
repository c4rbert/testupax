package com.example.testproject.screen.main.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testproject.databinding.FragmentSearcherBinding;
import com.example.testproject.screen.main.core.MainView;

public class SearcherFragment extends Fragment {

    private FragmentSearcherBinding binding;
    private MainView view;

    public SearcherFragment() {
    }

    public static SearcherFragment newInstance(MainView view) {
        SearcherFragment fragment = new SearcherFragment();
        fragment.view = view;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearcherBinding.inflate(inflater, container, false);
        setSearcherBtnListener();
        return binding.getRoot();
    }

    private void setSearcherBtnListener() {
        binding.search.setOnClickListener(view -> {
            String query = binding.movieSearcher.getText().toString();
            if(query.equals("")){
                this.view.showError();
            }else{
                binding.search.setVisibility(View.INVISIBLE);
                binding.progress.setVisibility(View.VISIBLE);
                this.view.searchMovie(query);
            }
        });
    }

    public void resetUI() {
        binding.search.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.INVISIBLE);
    }
}