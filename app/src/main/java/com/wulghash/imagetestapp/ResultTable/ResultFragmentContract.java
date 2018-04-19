package com.wulghash.imagetestapp.ResultTable;

import com.wulghash.imagetestapp.ResultImage;

/**
 * Created by Федор on 4/15/2018.
 */

public interface ResultFragmentContract {


    interface View {


        void onShowListDialog(ResultImage resultImage);
    }

    interface UserActionListener {

    }
}
