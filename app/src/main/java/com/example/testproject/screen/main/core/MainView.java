package com.example.testproject.screen.main.core;

import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.testproject.R;
import com.example.testproject.databinding.ActivityMainBinding;
import com.example.testproject.model.PictureBase64;
import com.example.testproject.model.Result;
import com.example.testproject.screen.main.MainActivity;
import com.example.testproject.screen.main.fragment.MapsFragment;
import com.example.testproject.screen.main.fragment.MoviesFragment;
import com.example.testproject.screen.main.fragment.PickerFragment;
import com.example.testproject.screen.main.fragment.SearcherFragment;

import java.util.List;

public class MainView {

    private MainActivity context;
    private MainPresenter presenter;
    private ActivityMainBinding binding;

    private FragmentManager fm;
    private Fragment prev;

    private SearcherFragment searcherFragment;
    private MoviesFragment moviesFragment;
    private MapsFragment mapsFragment;
    private PickerFragment pickerFragment;


    public MainView(MainActivity context) {
        this.context = context;
        initView();
        initFragmentsValues();
        initSearcherFragment();
        setBottomMenuListener();
    }


    public void setPresenter(MainPresenter presenter) {
        this.presenter = presenter;
    }

    private void initView() {
        binding = ActivityMainBinding.inflate(context.getLayoutInflater());
    }

    private void initFragmentsValues() {
        fm = context.getSupportFragmentManager();
        searcherFragment = SearcherFragment.newInstance(this);
        moviesFragment = MoviesFragment.newInstance(this);
        mapsFragment = MapsFragment.newInstance(this);
        pickerFragment = PickerFragment.newInstance(this);
    }

    private void initSearcherFragment() {
        setTitle(R.string.movies_title);
        newTransaction(searcherFragment);
    }

    private void setBottomMenuListener() {
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.searcherFragment:
                    initSearcherFragment();
                    break;
                case R.id.mapsFragment:
                    initMapsFragment();
                    break;
                case R.id.picturesFragment:
                    initPickerFragment();
                    break;
            }
            return true;
        });
    }

    public void initMoviesFragment(List<Result> movieListResponse, String query) {
        setTitle(query);
        searcherFragment.resetUI();
        moviesFragment.setMovieListResponse(movieListResponse);
        newTransaction(moviesFragment);
    }

    public void initMapsFragment() {
        setTitle(R.string.maps);
        mapsFragment.setLocationData(presenter.getDataSnapshot());
        newTransaction(mapsFragment);
    }

    public void initPickerFragment() {
        setTitle(R.string.pictures);
        newTransaction(pickerFragment);
    }



    public void newTransaction(Fragment fragment) {
        fm.beginTransaction()
                .replace(R.id.nav_host_fragment, fragment)
                .addToBackStack(null)
                .commit();
    }

    public MainActivity getContext() {
        return context;
    }

    public View getView() {
        return binding.getRoot();
    }

    private void setTitle(int title) {
        context.getSupportActionBar().setTitle(title);
    }

    private void setTitle(String title) {
        context.getSupportActionBar().setTitle(title);
    }

    public void showError() {
        Toast.makeText(context, R.string.movie_not_found, Toast.LENGTH_LONG).show();
        searcherFragment.resetUI();
    }

    public void searchMovie(String query) {
        presenter.searchMovie(query);
    }

    public void savePictures(List<PictureBase64> pictureBase64List) {
        presenter.savePictures(pictureBase64List);
    }

    public void showSavedPictures() {
        pickerFragment.showSavedPictures();
    }

    public String witOutConnection() {
        return context.getString(R.string.with_out_connection);
    }

    public void showLastSearch() {
        Toast.makeText(context, R.string.last_search, Toast.LENGTH_LONG).show();
    }
}
