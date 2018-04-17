package com.wulghash.imagetestapp.ImageWork;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
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
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;

import static android.app.Activity.RESULT_OK;


public class ImageFragment extends Fragment implements ImageFragmentContact.View {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

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
                new Dialog().getDialog(getActivity(),  1).show();
            }
        });
        chooseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Dialog().getDialog(getActivity(),  1).show();
                mainImage.setVisibility(View.VISIBLE);
                chooseImageButton.setVisibility(View.GONE);
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
    public void onMirrorImage() {
        Bitmap bitmap = ((BitmapDrawable)mainImage.getDrawable()).getBitmap();

        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        Matrix matrix = new Matrix();
        matrix.preScale(-1, 1);

        Bitmap flipImage = Bitmap.createBitmap(bitmap, 0,0 , width, height, matrix, true);

        mainImage.setImageBitmap(flipImage);
    }


    @Override
    public void showNoImageError() {
        Toast.makeText(getActivity(), "Select image first!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSelectNewImage(int item) {
        if (item == Dialog.MAKE_PHOTO) {
            onMakePhoto();
        } else if (item == Dialog.CHOOSE_IMAGE_FROM_GALLERY) {
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
        }

        if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
        }
    }

    @Override
    public void onRotateImage() {
        final RotateAnimation rotateAnim = new RotateAnimation(degrees, degrees + 90,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);

        rotateAnim.setDuration(0);
        rotateAnim.setFillAfter(true);
        mainImage.startAnimation(rotateAnim);
        degrees = degrees + 90;
    }

    @Override
    public void onInvertColors() {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        mainImage.setColorFilter(filter);

    }

    @Override
    public void onAddImageToResult() {
        ImageResultFragment imageResultFragment =  (ImageResultFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.list_fragment);
    }

    class Dialog {

         public static final int MAKE_PHOTO = 0;
         public static final int CHOOSE_IMAGE_FROM_GALLERY = 1;

         public static final int IDD_GET_IMAGE = 1; // Идентификаторы диалоговых окон
         public static final int IDD_ACTION_WITH_IMAGE = 2;


         public AlertDialog getDialog(Activity activity, int ID) {
             AlertDialog.Builder builder = new AlertDialog.Builder(activity);

             switch(ID) {
                case IDD_GET_IMAGE:
                  builder.setTitle(R.string.dlg_choose_image_title);
                  builder.setItems(R.array.choose_dlg_image_options, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        onSelectNewImage(which);
                    }
                });
                builder.setCancelable(true);

                return builder.create();
            case IDD_ACTION_WITH_IMAGE: // Диалоговое окно Rate the app
                return builder.create();
            default:
                return null;
        }
    }
}




}
