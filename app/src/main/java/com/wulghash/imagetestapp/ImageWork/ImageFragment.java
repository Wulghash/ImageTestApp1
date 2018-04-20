package com.wulghash.imagetestapp.ImageWork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.ResultImage;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;


public class ImageFragment extends Fragment implements ImageFragmentContact.View {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;


    public static final int MAKE_PHOTO = 0;
    public static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    public static final int GET_IMAGE_URL = 2;

    private ImageView mainImage;
    private Button rotateButton;
    private Button invertColorsButton;
    private Button mirrorImageColorsButton;
    private Button chooseImageButton;
    private ProgressBar progressBar;

    private ImageFragmentContact.UserActionListener imageFragmentPresenter;

    public ImageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_image, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
        progressBar.setVisibility(View.GONE);
        setRetainInstance(true);

//        final ProgressBar progressBar = getView().findViewById(R.id.progressBar3);

//        Observable.range(0,50)
//                .subscribeOn(Schedulers.computation())
//                .delay(1000, TimeUnit.MILLISECONDS)
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//                    @Override
//                    public void accept(Integer integer) throws Exception {
//                        progressBar.setProgress(integer);
//                    }
//                });


//   Observable.intervalRange(0L, 10L, 0, 1, TimeUnit.SECONDS,
//                Schedulers.newThread())
//                .subscribe(new DisposableObserver<Long>() {
//                    @Override
//                    public void onComplete() {
//                        getActivity().runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                Toast.makeText(getActivity(), "Нихуя ты умный",
//                                        Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//
//                    @Override
//                    public void onNext(Long o) {
//                        long l = o;
//                        int i = (int) l;
//                        progressBar.setProgress(i);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//                });

    }

    private void setupButtons() {
        mainImage = getView().findViewById(R.id.main_image);
        progressBar = getView().findViewById(R.id.progressBar);

        chooseImageButton = getView().findViewById(R.id.choose_image_button);
        rotateButton = getView().findViewById(R.id.rotate_btn);
        invertColorsButton = getView().findViewById(R.id.invert_btn);
        mirrorImageColorsButton = getView().findViewById(R.id.mirror_btn);

        mainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChooseImageDialog().show();
            }
        });
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getChooseImageDialog().show();
            }
        });
        rotateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onRotateImage();
            }
        });
        invertColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onInvertColors();
            }
        });
        mirrorImageColorsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMirrorImage();
            }
        });

    }
    @Override
    public void showNoImageError() {
        Toast.makeText(getActivity(), "Select imageView first!",
                Toast.LENGTH_SHORT).show();
    }


    private void test(String url) {
        Request request = new Request.Builder()
                .url(url)
                .build();
        OkHttpClient okHttpClient = new OkHttpClient();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {}

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    int current;
                    while ((current = response.body().byteStream().read()) != -1) {
                        outputStream.write((byte) current);
                    }
                    byte[] array = outputStream.toByteArray();
                    outputStream.close();
               final Bitmap bitmap = BitmapFactory.decodeByteArray(array , 0, array.length);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        chooseImageButton.setVisibility(View.GONE);
                        mainImage.setImageBitmap(bitmap);
                        mainImage.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        okHttpClient.connectionPool().evictAll();




    }

    @Override
    public void onSelectNewImage(int item) {
        if (item == MAKE_PHOTO) {
            onMakePhoto();
        } else if (item == CHOOSE_IMAGE_FROM_GALLERY) {
            onChooseImage();
        } else if (item == GET_IMAGE_URL) {
            getUrlDIalog().show();
        }
    }

    private void onMakePhoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    private void onChooseImage() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        if (photoPickerIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(photoPickerIntent, REQUEST_IMAGE_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            mainImage.setImageBitmap(imageBitmap);
            chooseImageButton.setVisibility(View.GONE);
            mainImage.setVisibility(View.VISIBLE);
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                mainImage.setImageBitmap(selectedImage);
                chooseImageButton.setVisibility(View.GONE);
                mainImage.setVisibility(View.VISIBLE);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onRotateImage() {
        if (mainImage.getVisibility() != View.GONE)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.ROTATE_MODE);
        else showNoImageError();
    }

    @Override
    public void onInvertColors() {
        if (mainImage.getVisibility() != View.GONE)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.INVERT_MODE);
        else showNoImageError();
    }

    @Override
    public void onMirrorImage() {
        if (mainImage.getVisibility() != View.GONE)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.MIRROR_MODE);
        else showNoImageError();
    }

    @Override
    public void onAddImageToResult(Bitmap result, int mode) {
        ImageResultFragment imageResultFragment =  (ImageResultFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.list_fragment);
        imageResultFragment.addImage(new ResultImage(result, mode));
    }


    public void setSelectedImage(ResultImage resultImage) {
        mainImage.setImageBitmap(resultImage.getBitmap());
    }


    public AlertDialog getChooseImageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dlg_choose_image_title);
        builder.setItems(R.array.choose_dlg_image_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onSelectNewImage(which);
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }

    public AlertDialog getUrlDIalog() {
        String url = "";
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.type_url_title);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText("https://pp.userapi.com/c7002/v7002760/46f64/GmEkmdSmepA.jpg");
        builder.setView(input);

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                new ImageDownload().execute(new String[]{input.getText().toString()});
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        return builder.create();
    }

    private class ImageDownload extends AsyncTask<String , Void, Void>{

        @Override
        protected Void doInBackground(String[] params) {
            test(params[0]);
            return null;
        }
        @Override
        protected void onPostExecute(Void result) {
            progressBar.setVisibility(View.GONE);
            mainImage.setVisibility(View.VISIBLE);
            super.onPostExecute(result);
        }
        @Override
        protected void onPreExecute() {
            chooseImageButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

    }




}
