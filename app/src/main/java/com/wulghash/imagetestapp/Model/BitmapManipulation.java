package com.wulghash.imagetestapp.Model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;

import rx.Observable;
import rx.schedulers.Schedulers;

public class BitmapManipulation {



    public static Observable<Bitmap> getResultBitmap(ResultImage resultImage) {
        if (resultImage.getMode() == ResultImage.ROTATE_MODE) return Observable.just(getRotatedImageBitmap(resultImage.getBitmap()))
                                                                               .subscribeOn(Schedulers.computation());
        else if (resultImage.getMode() == ResultImage.INVERT_MODE) return Observable.just(getInvertedImageBitmap(resultImage.getBitmap()))
                                                                                     .subscribeOn(Schedulers.computation());
        else if (resultImage.getMode() == ResultImage.MIRROR_MODE) return  Observable.just(getMirrorImageBitmap(resultImage.getBitmap()))
                                                                                      .subscribeOn(Schedulers.computation());
        else return null;
    }

    private static Bitmap getRotatedImageBitmap(Bitmap bitmap) {
        Bitmap bitmapToUse = compressBitmapIfNeeded(bitmap);
        Matrix matrix = new Matrix();
        matrix.postRotate(90);

        Bitmap rotated = Bitmap.createBitmap(bitmapToUse, 0, 0, bitmapToUse.getWidth(), bitmapToUse.getHeight(),
                matrix, true);
        return rotated;
    }


    private static Bitmap getMirrorImageBitmap(Bitmap bitmap) {
        Bitmap bitmapToUse = compressBitmapIfNeeded(bitmap);
        int width = bitmapToUse.getWidth();
        int height = bitmapToUse.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);
        Bitmap flipImage = Bitmap.createBitmap(bitmapToUse, 0,0 , width, height, matrix, true);
        return flipImage;
    }


    private static Bitmap getInvertedImageBitmap(Bitmap bitmap) {
        Bitmap bitmapToUse = compressBitmapIfNeeded(bitmap);
        ColorMatrix bwMatrix =new ColorMatrix();
        bwMatrix.setSaturation(0);
        final ColorMatrixColorFilter colorFilter= new ColorMatrixColorFilter(bwMatrix);
        Bitmap rBitmap = bitmapToUse.copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setColorFilter(colorFilter);
        Canvas myCanvas = new Canvas(rBitmap);
        myCanvas.drawBitmap(rBitmap, 0, 0, paint);
        return rBitmap;
    }


    private static Bitmap compressBitmapIfNeeded(Bitmap bitmap) {

        Bitmap bitmapToUse = null;

        if (bitmap.getWidth() + bitmap.getHeight() > 1000) {
            double bitmapRatio = (double) bitmap.getWidth() / bitmap.getHeight();
            int resultWidth = (int) Math.round(250 * bitmapRatio);
            bitmapToUse = Bitmap.createScaledBitmap(bitmap,  resultWidth, 250, true);
        } else {
            bitmapToUse = bitmap;
        }
        return bitmapToUse;
    }


}
