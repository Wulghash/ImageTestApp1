package com.wulghash.imagetestapp.ImageWork;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

/**
 * Created by Федор on 4/15/2018.
 */

public interface ImageFragmentContact {

    interface View {

        void showNoImageError();

        void onSelectNewImage(int item);

        void onRotateImage();

        void onMirrorImage();

        void onInvertColors();

        void onAddImageToResult(Bitmap bitmap, int mode);

    }

    interface  UserActionListener {

        void setView(ImageFragmentContact.View view);

        Bitmap getPhoto(Intent intent);

        Bitmap getImageFromSources(Intent intent, Context context);

        void getImageFromUrl(String url, ProgressBar progressBar, ImageView imageView, Button button, FragmentActivity context);

    }
}
