package com.wulghash.imagetestapp.ResultTable;

import com.wulghash.imagetestapp.Model.ResultImageDatabaseHelper;
import com.wulghash.imagetestapp.Model.ResultImage;

/**
 * Created by Федор on 4/15/2018.
 */

public interface ResultFragmentContract {


    interface View {

        void addSavedImagesFromBd(ResultImage resultImage);

        void addImageToResult(ResultImage resultImage);

        void onShowListDialog(ResultImage resultImage);
    }

    interface UserActionListener {

        void onDeleteImage(ResultImage resultImage, ResultImageDatabaseHelper helper);

        void onLoadSavedImages(ResultImageDatabaseHelper helper);

        void onSaveNewImage(ResultImage resultImage, ResultImageDatabaseHelper helper);
    }
}
