package com.wulghash.imagetestapp.ImageWork;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.ResultImage;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static android.app.Activity.RESULT_OK;


public class ImageFragment extends Fragment implements ImageFragmentContact.View {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    public static final int MAKE_PHOTO = 0;
    public static final int CHOOSE_IMAGE_FROM_GALLERY = 1;



    float degrees = 0.0f;

    private ImageView mainImage;
    private Button rotateButton;
    private Button invertColorsButton;
    private Button mirrorImageColorsButton;
    private Button chooseImageButton;

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
        setRetainInstance(true);
    }

    private void setupButtons() {
        mainImage = getView().findViewById(R.id.main_image);

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

    @Override
    public void onSelectNewImage(int item) {
        if (item == MAKE_PHOTO) {
            onMakePhoto();
        } else if (item == CHOOSE_IMAGE_FROM_GALLERY) {
            onChooseImage();
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




}
