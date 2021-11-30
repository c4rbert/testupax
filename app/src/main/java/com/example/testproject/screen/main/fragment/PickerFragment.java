package com.example.testproject.screen.main.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.ParcelFileDescriptor;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.testproject.R;
import com.example.testproject.databinding.FragmentPickerBinding;
import com.example.testproject.model.PictureBase64;
import com.example.testproject.screen.main.adapter.PicturesAdapter;
import com.example.testproject.screen.main.core.MainView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import gun0912.tedimagepicker.builder.TedImagePicker;

public class PickerFragment extends Fragment {

    private String TAG = this.getClass().getSimpleName();
    private FragmentPickerBinding binding;
    private MainView view;
    private List<PictureBase64> pictureBase64List = new ArrayList<>();
    private PicturesAdapter picturesAdapter;

    public PickerFragment() {
    }

    public static PickerFragment newInstance(MainView view) {
        PickerFragment fragment = new PickerFragment();
        fragment.view = view;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPickerBinding.inflate(inflater, container, false);
        setPictureListener();
        initRecyclerView();
        return binding.getRoot();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void setPictureListener() {
        binding.picture.setOnClickListener(view1 -> {
            binding.progress.setVisibility(View.VISIBLE);
            binding.picture.setVisibility(View.INVISIBLE);
            TedImagePicker.with(getContext())
                    .max(10, R.string.max_pictures)
                    .startMultiImage(uriList -> {
                        new Thread(() -> {
                            for (Uri uri : uriList) {
                                try {
                                    createBitmapFromUri(uri);
                                } catch (IOException e) {
                                    Log.e(TAG, e.getMessage());
                                }
                            }
                            view.savePictures(pictureBase64List);
                        }).start();
                    });
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.N)
    private void createBitmapFromUri(Uri imageUri) throws IOException {
        Bitmap selectedImage = getStreamByteFromImage(imageUri);
        new Thread(() -> {
            String base64ImageClient = encodeImage(selectedImage);
            pictureBase64List.add(new PictureBase64(base64ImageClient));
        }).start();
    }

    private String encodeImage(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String encImage = Base64.encodeToString(b, Base64.DEFAULT);

        return encImage;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public Bitmap getStreamByteFromImage(final Uri imageFile) {

        try (ParcelFileDescriptor pfd = view.getContext().getContentResolver().openFileDescriptor(imageFile, "r")) {
            if (null != pfd) {
                Bitmap photoBitmap = BitmapFactory.decodeFileDescriptor(pfd.getFileDescriptor());
                ByteArrayOutputStream stream = new ByteArrayOutputStream();

                int imageRotation = getImageRotation(pfd);

                photoBitmap = getBitmapRotatedByDegree(photoBitmap, imageRotation);

                return photoBitmap;
            } else {
                return null;
            }
        } catch (Exception ex) {
            return null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int getImageRotation(final ParcelFileDescriptor pfd) {

        ExifInterface exif = null;
        int exifRotation = 0;

        try {
            exif = new ExifInterface(pfd.getFileDescriptor());
            exifRotation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }

        if (exif == null)
            return 0;
        else
            return exifToDegrees(exifRotation);
    }

    private int exifToDegrees(int rotation) {
        if (rotation == ExifInterface.ORIENTATION_ROTATE_90)
            return 90;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_180)
            return 180;
        else if (rotation == ExifInterface.ORIENTATION_ROTATE_270)
            return 270;

        return 0;
    }

    private Bitmap getBitmapRotatedByDegree(Bitmap bitmap, int rotationDegree) {
        Matrix matrix = new Matrix();

        int width = bitmap.getWidth();
        int newWidth = 320;

        // calculate the scale - in this case = 0.4f
        float scaleWidth = ((float) newWidth) / width;

        // resize the bit map
        matrix.postScale(scaleWidth, scaleWidth);

        matrix.preRotate(rotationDegree);

        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    private void initRecyclerView() {
        picturesAdapter = new PicturesAdapter(pictureBase64List);
        binding.picturesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        binding.picturesRecyclerView.setAdapter(picturesAdapter);
    }

    public void showSavedPictures() {
        picturesAdapter.notifyDataSetChanged();
        binding.picture.setVisibility(View.VISIBLE);
        binding.progress.setVisibility(View.INVISIBLE);
    }
}