package com.example.somativa02;
import android.net.Uri;

public class PhotoSave {
    public Uri uri;
    public String title;
    public String description;
    public String deepText;

    public PhotoSave(Uri uri, String title, String description, String deepText) {
        this.uri = uri;
        this.title = title;
        this.description = description;
        this.deepText = deepText;
    }
}
