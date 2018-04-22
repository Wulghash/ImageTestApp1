package com.wulghash.imagetestapp.Model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Subscriber;
import rx.schedulers.Schedulers;


import static android.content.ContentValues.TAG;

public class ResultImageDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "image_test_app";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "RESULT_IMAGE";


    private static final String ID_COLUMN = "_id";
    private static final String IMAGE_PATH_COLUMN = "PATH";


    public ResultImageDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RESULT_IMAGE ("
                + "_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + "PATH TEXT); ");
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private Callable<Long> insertImageToBd(final SQLiteDatabase db, final ResultImage resultImage) {

        return new Callable<Long>() {
            @Override
            public Long call() throws Exception {
                Uri resultUri = DbResultImageUtility.saveImageToStorage(resultImage.getBitmap());
                resultImage.setUri(resultUri);
                ContentValues values = new ContentValues();
                values.put(IMAGE_PATH_COLUMN, resultUri.toString());
                return db.insert(TABLE_NAME, null, values);
            }
        };
    }




    private Callable<Void> deleteImage1(final SQLiteDatabase db, final long id) {
        return new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                db.execSQL("delete from RESULT_IMAGE where _id = " + id);
                return null;
            }
        };
    }


     private Callable<List<ResultImage>> getResultImages(final SQLiteDatabase db) {
        return new Callable<List<ResultImage>>() {
            @Override
            public List<ResultImage> call() throws Exception {
                List<ResultImage> list = new ArrayList<>();
                Cursor cursor = db.query(TABLE_NAME,
                        new String[] {ID_COLUMN, IMAGE_PATH_COLUMN},
                        null,
                        null,
                        null, null,null);
                if (cursor.moveToFirst()){
                    do {
                        ResultImage resultImage = new ResultImage();
                        Uri uri = Uri.parse(cursor.getString(cursor.getColumnIndex(IMAGE_PATH_COLUMN)));
                        long id = cursor.getLong(cursor.getColumnIndex(ID_COLUMN));
                        resultImage.setId(id);
                        resultImage.setUri(uri);
                        list.add(resultImage);
                    } while(cursor.moveToNext());
                }
                return list;
            }
        };
    }

    private static <T> rx.Observable<T> makeObservable(final Callable<T> func) {
        return rx.Observable.create(
                new rx.Observable.OnSubscribe<T>() {
                    @Override
                    public void call(Subscriber<? super T> subscriber) {
                        try {
                            subscriber.onNext(func.call());
                        } catch(Exception ex) {
                            Log.e(TAG, "Error reading from the database", ex);
                        }
                    }
                });
    }


   public rx.Observable<List<ResultImage>> getImages() {
        return makeObservable(getResultImages(getReadableDatabase()))
                .subscribeOn(Schedulers.computation()); // note: do not use Schedulers.io()
    }


    public rx.Observable<Long> insertImage(SQLiteDatabase db, ResultImage resultImage) {
        return makeObservable(insertImageToBd(db, resultImage)).subscribeOn(Schedulers.computation());
    }


    public rx.Observable<Void> deleteImage(SQLiteDatabase db, long id) {
        return makeObservable(deleteImage1(db, id)).subscribeOn(Schedulers.computation());
    }





}
