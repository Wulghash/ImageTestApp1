package com.wulghash.imagetestapp;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wulghash.imagetestapp.ImageWork.ImageFragment;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;


public class MainActivity extends AppCompatActivity {

    private ImageFragment imageFragment;
    private ImageResultFragment imageResultFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            initFragments();
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},23
            );
        }

    }

    private void initFragments() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        imageFragment = new ImageFragment();
        imageResultFragment = new ImageResultFragment();
        transaction.replace(R.id.image_fragment, imageFragment);
        transaction.replace(R.id.list_fragment, imageResultFragment);
        transaction.commit();

    }

}
