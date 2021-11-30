package com.example.testproject.screen.main.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testproject.R;
import com.example.testproject.databinding.FragmentMapsBinding;
import com.example.testproject.model.User;
import com.example.testproject.screen.main.core.MainView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;

public class MapsFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FragmentMapsBinding binding;
    private MainView view;
    private DataSnapshot dataSnapshot;

    public MapsFragment() {
    }

    public static MapsFragment newInstance(MainView view) {
        MapsFragment fragment = new MapsFragment();
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
        binding = FragmentMapsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if(dataSnapshot != null){
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                User user = snapshot.getValue(User.class);
                LatLng marker = new LatLng(user.getLatitude(), user.getLongitude());
                mMap.addMarker(new MarkerOptions().position(marker).title(user.getName()));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(marker, 14));
            }
        }
    }

    public void setLocationData(DataSnapshot dataSnapshot) {
        this.dataSnapshot = dataSnapshot;
    }
}