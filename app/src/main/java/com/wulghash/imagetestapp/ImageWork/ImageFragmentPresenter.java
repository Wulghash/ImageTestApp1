package com.wulghash.imagetestapp.ImageWork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wulghash.imagetestapp.Model.ImageSources;


/**
 * Created by Федор on 4/15/2018.
 */

public class ImageFragmentPresenter implements ImageFragmentContact.UserActionListener {

    ImageFragmentContact.View view;

    public ImageFragmentPresenter(ImageFragmentContact.View view) {
        this.view = view;
    }

    @Override
    public void setView(ImageFragmentContact.View view) {
        this.view = view;
    }


    @Override
    public Bitmap getPhoto(Intent intent) {
        return ImageSources.getPhotoBitmap(intent);
    }

    @Override
    public Bitmap getImageFromSources(Intent intent, Context context) {
        return ImageSources.getBitmapImageFromGallery(intent, context);
    }

    @Override
    public void getImageFromUrl(String url, ProgressBar progressBar, ImageView imageView, Button button, FragmentActivity context) {
        ImageSources.getBitmapFromURL(url, progressBar, imageView, button,  context);
    }
}
