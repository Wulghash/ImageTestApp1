package com.wulghash.imagetestapp.Model;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.media.session.IMediaControllerCallback;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func0;
import rx.schedulers.Schedulers;


public class ImageSources {


    public static Bitmap getPhotoBitmap(Intent data) {
        Bundle extras = data.getExtras();
        Bitmap imageBitmap = (Bitmap) extras.get("data");
        return imageBitmap;
    }


    public static Bitmap getBitmapImageFromGallery(Intent data, Context context) {
        try {
            Uri imageUri = data.getData();
            InputStream imageStream = context.getContentResolver().openInputStream(imageUri);
            Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            return selectedImage;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void getBitmapFromURL(final String url, final ProgressBar progressBar, final ImageView imageView, final Button button, final FragmentActivity context) {
        progressBar.setVisibility(View.VISIBLE);
        button.setVisibility(View.GONE);
        final OkHttpClient client = new OkHttpClient();
        Observable.defer(new Func0<Observable<Response>>() {
            @Override public Observable<Response> call() {
                try {
                    Response response = client.newCall(new Request.Builder().url(url).build()).execute();
                    return Observable.just(response);
                } catch (IOException e) {
                    return Observable.error(e);
                }
            }
              }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Response>() {
            @Override
            public void onCompleted() {
                imageView.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable e) {
                Toast.makeText(context, "URL is not valid!", Toast.LENGTH_SHORT).show();
                imageView.setVisibility(View.GONE);
                button.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onNext(Response response) {
                try {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int current;
                    while ((current = response.body().byteStream().read()) != -1) {
                        outputStream.write((byte) current);
                    }
                    byte[] array = outputStream.toByteArray();
                    outputStream.close();
                    final Bitmap bitmap = BitmapFactory.decodeByteArray(array, 0, array.length);
                    imageView.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        }

}
