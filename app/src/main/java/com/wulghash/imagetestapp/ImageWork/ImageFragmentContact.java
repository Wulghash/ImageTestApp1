package com.wulghash.imagetestapp.ImageWork;

/**
 * Created by Федор on 4/15/2018.
 */

public interface ImageFragmentContact {

    interface View {

        void showNoImageError();

        void onSelectNewImage();

        void onRotateImage();

        void onMirrorImage();

        void onInvertColors();

    }

    interface  UserActionListener {

        void onAddImageToResult();
    }
}
