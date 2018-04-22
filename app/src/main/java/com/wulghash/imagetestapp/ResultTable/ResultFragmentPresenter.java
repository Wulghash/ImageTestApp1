package com.wulghash.imagetestapp.ResultTable;


import android.graphics.Bitmap;

import com.wulghash.imagetestapp.Model.BitmapManipulation;
import com.wulghash.imagetestapp.Model.DbResultImageUtility;
import com.wulghash.imagetestapp.Model.ResultImageDatabaseHelper;
import com.wulghash.imagetestapp.Model.ResultImage;

import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

/**
 * Created by Федор on 4/15/2018.
 */

public class ResultFragmentPresenter implements ResultFragmentContract.UserActionListener {

    ResultFragmentContract.View view;

    public ResultFragmentPresenter(ResultFragmentContract.View view) {
        this.view = view;
    }

    @Override
    public void onSaveNewImage(final ResultImage resultImage, final ResultImageDatabaseHelper helper) {
        BitmapManipulation.getResultBitmap(resultImage)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Bitmap>() {
                               @Override
                               public void call(Bitmap bitmap) {
                                   resultImage.setBitmap(bitmap);
                                   helper.insertImage(helper.getWritableDatabase(), resultImage)
                                           .observeOn(AndroidSchedulers.mainThread())
                                           .subscribe(new Action1<Long>() {
                                               @Override
                                               public void call(Long aLong) {
                                                   resultImage.setId(aLong);
                                                   view.addImageToResult(resultImage);
                                               }
                                           });
                               }
                           });


    }

    @Override
    public void onLoadSavedImages(ResultImageDatabaseHelper helper) {
        helper.getImages()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ResultImage>>() {
                    @Override
                    public void call(List<ResultImage> uris) {
                        for (ResultImage resultImage : uris) {
                            view.addSavedImagesFromBd(resultImage);
                        }
                    }
                });
    }

    @Override
    public void onDeleteImage(ResultImage resultImage, ResultImageDatabaseHelper helper) {
        helper.deleteImage(helper.getWritableDatabase(), resultImage.getId())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
        DbResultImageUtility.deleteImageFromDir(resultImage.getUri());
    }



}
