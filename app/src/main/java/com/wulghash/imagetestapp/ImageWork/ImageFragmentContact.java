package com.wulghash.imagetestapp.ImageWork;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

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

    }
}
