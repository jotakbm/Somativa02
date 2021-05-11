package com.example.somativa02;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class PhotoScreen extends AppCompatActivity {
    static final int PERMISSION_CODE = 1001;
    static final int GALLERY_INTENT_CODE = 1002;
    static final int CAMERA_INTENT_CODE = 1003;
    static PhotoSave actualInfo;
    SharedPreferences prefs;
    ImageView imageViewCamera;
    TextView titleText;
    TextView descriptionText;
    TextView deepText;
    Button newPhotoButton;
    Button savePhotoButton;
    Button deletePhotoButton;
    SeekBar seekbar;
    Uri image_uri;
    String originalTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Photo");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_screen);
        imageViewCamera = findViewById(R.id.imageView_Photo);
        titleText = findViewById(R.id.editText_Title);
        descriptionText = findViewById(R.id.editText_Description);
        deepText = findViewById(R.id.editText_DeepText);
        newPhotoButton = findViewById(R.id.button_NewPhoto);
        savePhotoButton = findViewById(R.id.button_SavePhoto);
        deletePhotoButton = findViewById(R.id.button_DeletePhoto);
        deletePhotoButton.setEnabled(actualInfo != null);
        deletePhotoButton.setCursorVisible(actualInfo != null);
        originalTitle = titleText.getText().toString();
        seekbar = findViewById(R.id.seekBar);

        prefs = getPreferences(MODE_PRIVATE);
        float fs = prefs.getFloat("fontsize", 12);
        seekbar.setProgress((int) fs);
        deepText.setTextSize(TypedValue.COMPLEX_UNIT_PX, seekbar.getProgress());

        if (actualInfo != null) {
            titleText.setText(actualInfo.title);
            descriptionText.setText(actualInfo.description);
            deepText.setText(actualInfo.deepText);
            imageViewCamera.setImageURI(actualInfo.uri.normalizeScheme());
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                prefs = getPreferences(MODE_PRIVATE);
                SharedPreferences.Editor ed = prefs.edit();
                ed.putFloat("fontsize", deepText.getTextSize());
                ed.commit();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                int finalValue = Math.min(100, progress + 20);
                deepText.setTextSize(TypedValue.COMPLEX_UNIT_PX, finalValue);
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        actualInfo = null;
    }

    public void onButtonCameraPressed(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            requestCameraPermissions();
        else
            sendCameraIntent();
    }

    public void onGalleryPressed(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, GALLERY_INTENT_CODE);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    void requestCameraPermissions() {
        if (getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
                    checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PERMISSION_CODE);
            } else {
                sendCameraIntent();
            }
        } else {
            Toast.makeText(this, "No camera available", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                sendCameraIntent();
            else
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_LONG).show();
        }
    }

    void sendCameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent cameraItent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraItent.putExtra(MediaStore.EXTRA_OUTPUT, image_uri);
        startActivityForResult(cameraItent, CAMERA_INTENT_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_INTENT_CODE)
            image_uri = data.getData();
        imageViewCamera.setImageURI(image_uri);
        Toast.makeText(this, image_uri.toString(), Toast.LENGTH_LONG).show();
    }

    public void onButtonSaveClicked(View view) {
        String title = titleText.getText().toString();
        String description = descriptionText.getText().toString();
        String deep = deepText.getText().toString();
        if (stringCount(title) < 1) {
            Toast.makeText(getApplicationContext(), "Please enter a valid title.", Toast.LENGTH_SHORT).show();
            return;
        }
        else if((actualInfo != null && !title.equals(actualInfo.title)) && PhotoManager.getInstance().CheckTitlePhoto(title)){
            Toast.makeText(getApplicationContext(), "This title already exists, please enter a new title.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (actualInfo != null) {
            actualInfo.title = title;
            actualInfo.description = description;
            actualInfo.deepText = deep;
            actualInfo.uri = image_uri;
            Toast.makeText(getApplicationContext(), '"' + title + '"' + " has been successfully updated.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), '"' + title + '"' + " was successfully created.", Toast.LENGTH_SHORT).show();
            PhotoManager.getInstance().AddPhoto(new PhotoSave(image_uri, title, description, deep));
        }
        finish();
    }

    public void OnButtonDeleteClicked(View view) {
        String title = titleText.getText().toString();
        PhotoManager.getInstance().RemovePhoto(actualInfo);
        Toast.makeText(getApplicationContext(), '"' + title + '"' + " has been successfully deleted.", Toast.LENGTH_SHORT).show();
        finish();
    }

    private int stringCount(String text) {
        int count = 0;

        //Counts each character except space
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) != ' ')
                count++;
        }
        return count;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Attention!");
        builder.setMessage("Do you really want to leave without saving changes?");
        builder.setPositiveButton(android.R.string.no, null);
        builder.setNegativeButton(android.R.string.yes,
                new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i){
                        finish();
                    }
                });

        builder.create().show();
    }
}