package com.wulghash.imagetestapp.ImageWork;

/**
 * Created by Федор on 4/15/2018.
 */

public class ImageFragmentPresenter implements ImageFragmentContact.UserActionListener {

    ImageFragmentContact.View view;

    @Override
    public void setView(ImageFragmentContact.View view) {
        this.view = view;
    }
}
