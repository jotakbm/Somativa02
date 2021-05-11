package com.example.somativa02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    RecyclerView imageRecyclerView;
    Adapter adapter;
    Button goPhotoScreenButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Gallery");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        goPhotoScreenButton = findViewById(R.id.button_GoPhotoScreen);
        imageRecyclerView = findViewById(R.id.RecyclerView_Images);
        imageRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshGallery();
    }

    public void refreshGallery(){
        adapter = new Adapter(this, PhotoManager.getInstance().photoList);
        imageRecyclerView.setAdapter(adapter);
    }

    public void onGoPhotoScreenPressed(View view) {
        Intent intent = new Intent(this, PhotoScreen.class);
        startActivity(intent);
    }
}