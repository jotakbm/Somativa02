package com.example.somativa02;

import android.os.Bundle;

import java.util.ArrayList;

public class PhotoManager {
    private static PhotoManager instance;
    public static ArrayList<PhotoSave> photoList = new ArrayList<>();

    public static PhotoManager getInstance() {

        if (instance == null) {
            instance = new PhotoManager();
            onCreate();
        }

        return instance;
    }

    private static void onCreate() {
        photoList.add(new PhotoSave(null, "Title01", "Description01", "Example01"));
        photoList.add(new PhotoSave(null, "Title02", "Description02", "Example02"));
    }

    public void AddPhoto(PhotoSave info) {
        photoList.add(info);
    }

    public boolean CheckTitlePhoto(String title) {
        return photoList.contains(GetPhoto(title));
    }
    
    public void RemovePhoto(PhotoSave info) {
        photoList.remove(info);
    }

    public PhotoSave GetPhoto(String title) {
        for (PhotoSave user : photoList) {
            if (title.equals(user.title))
                return user;
        }
        return null;
    }
}
