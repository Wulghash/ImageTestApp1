package com.wulghash.imagetestapp.ImageWork;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.Model.ResultImage;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;


import static android.app.Activity.RESULT_OK;


public class ImageFragment extends Fragment implements ImageFragmentContact.View {


    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;


    public static final int MAKE_PHOTO = 0;
    public static final int CHOOSE_IMAGE_FROM_GALLERY = 1;
    public static final int GET_IMAGE_URL = 2;

    private ImageView mainImage;
    private Button chooseImageButton;
    private ProgressBar progressBar;

    private Bitmap currentBitmap;

    private ImageFragmentContact.UserActionListener imageFragmentPresenter;

    public ImageFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        imageFragmentPresenter = new ImageFragmentPresenter(this);
        setRetainInstance(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_image, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupButtons();
        progressBar.setVisibility(View.GONE);


    }

    private void setupButtons() {
        mainImage = getView().findViewById(R.id.main_image);
        chooseImageButton = getView().findViewById(R.id.choose_image_button);
        if (currentBitmap != null) {
            mainImage.setImageBitmap(currentBitmap);
            mainImage.setVisibility(View.VISIBLE);
            chooseImageButton.setVisibility(View.GONE);
        }
        progressBar = getView().findViewById(R.id.progressBar);
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
        Button rotateButton = getView().findViewById(R.id.rotate_btn);
        Button invertColorsButton = getView().findViewById(R.id.invert_btn);
        Button mirrorImageColorsButton = getView().findViewById(R.id.mirror_btn);

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
        Toast.makeText(getActivity(), "Select image first!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDetach() {
        if (mainImage.getDrawable() != null)
        currentBitmap = ((BitmapDrawable)mainImage.getDrawable()).getBitmap();
        super.onDetach();
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
            mainImage.setImageBitmap(imageFragmentPresenter.getPhoto(data));
        } else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            mainImage.setImageBitmap(imageFragmentPresenter.getImageFromSources(data, getActivity()));
        }
        if (resultCode == RESULT_OK) {
            chooseImageButton.setVisibility(View.GONE);
            mainImage.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onRotateImage() {
        if (mainImage.getVisibility() != View.GONE || mainImage.getDrawable() != null)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.ROTATE_MODE);
        else showNoImageError();
    }

    @Override
    public void onInvertColors() {
        if (mainImage.getVisibility() != View.GONE || mainImage.getDrawable() != null)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.INVERT_MODE);
        else showNoImageError();
    }

    @Override
    public void onMirrorImage() {
        if (mainImage.getVisibility() != View.GONE || mainImage.getDrawable() != null)
            onAddImageToResult(((BitmapDrawable)mainImage.getDrawable()).getBitmap(), ResultImage.MIRROR_MODE);
        else showNoImageError();
    }

    @Override
    public void onAddImageToResult(Bitmap result, int mode) {
        if (getActivity() != null) {
            ImageResultFragment imageResultFragment = (ImageResultFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.list_fragment);
            imageResultFragment.addImage(new ResultImage(result, mode));
        }
    }


    public void setSelectedImage(ResultImage resultImage) {
      mainImage.setImageURI(resultImage.getUri());
        if (mainImage.getVisibility() != View.VISIBLE) {
            mainImage.setVisibility(View.VISIBLE);
            chooseImageButton.setVisibility(View.GONE);
        }
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.type_url_title);

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText("https://pp.userapi.com/c626229/v626229635/3a06b/XPIAhwxvrkM.jpg");
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    imageFragmentPresenter.getImageFromUrl(input.getText().toString(), progressBar, mainImage, chooseImageButton, getActivity());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                chooseImageButton.setVisibility(View.VISIBLE);
            }
        });

        return builder.create();
    }

}
