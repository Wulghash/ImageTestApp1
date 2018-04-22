package com.wulghash.imagetestapp.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;

import java.io.File;

public class ResultImage {

    public static final int ROTATE_MODE = 1;
    public static final int INVERT_MODE = 2;
    public static final int MIRROR_MODE = 3;

    private long id;
    private Bitmap bitmap;
    //Что предстоит сделать с этим изображением
    private int mode;

    private Uri uri;

    private int progress;
    private int processSpeed;

    private boolean isDownloaded= false;

    public ResultImage(Bitmap bitmap, int mode) {
        this.bitmap = bitmap;
        this.mode = mode;
    }

    public ResultImage() {

    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public int getMode() {
        return mode;
    }


    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


    public Uri getUri() {
        if (uri != null)
        return uri;
        else  {
            File file;
            String path = Environment.getExternalStorageDirectory().toString();
            file = new File(path, bitmap.toString()+".jpg");
            return Uri.parse(file.getAbsolutePath());
        }
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public boolean isDownloaded() {
        return isDownloaded;
    }

    public void setDownloaded(boolean downloaded) {
        isDownloaded = downloaded;
    }

    public int getProcessSpeed() {
        return processSpeed;
    }

    public void setProcessSpeed(int processSpeed) {
        this.processSpeed = processSpeed;
    }
}
