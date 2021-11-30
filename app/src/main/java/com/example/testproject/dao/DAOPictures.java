package com.example.testproject.dao;

import androidx.annotation.NonNull;

import com.example.testproject.model.PictureBase64;
import com.example.testproject.screen.main.core.MainPresenter;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class DAOPictures {

    private DatabaseReference databaseReference;
    private MainPresenter mainPresenter;

    public DAOPictures(){
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        databaseReference = db.getReference(PictureBase64.class.getSimpleName());
    }

    public Task<Void> add(List<PictureBase64> pictureBase64List){
        return databaseReference.push().setValue(pictureBase64List);
    }
}
