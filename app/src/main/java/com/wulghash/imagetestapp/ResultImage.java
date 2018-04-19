package com.wulghash.imagetestapp;

import android.graphics.Bitmap;
import android.widget.ImageView;

public class ResultImage {

    public static final int ROTATE_MODE = 1;
    public static final int INVERT_MODE = 2;
    public static final int MIRROR_MODE = 3;

    private Bitmap bitmap;
    //Что предстоит сделать с этим изображением
    private int mode;



    public ResultImage(Bitmap bitmap, int mode) {
        this.bitmap = bitmap;
        this.mode = mode;
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



}
