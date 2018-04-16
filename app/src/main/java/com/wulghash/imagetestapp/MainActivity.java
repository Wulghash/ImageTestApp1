package com.wulghash.imagetestapp;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wulghash.imagetestapp.ImageWork.ImageFragment;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;
import com.wulghash.imagetestapp.ResultTable.dummy.DummyContent;

public class MainActivity extends AppCompatActivity implements ImageResultFragment.OnListFragmentInteractionListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageFragment fragment = new ImageFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.image_fragment, fragment);
        transaction.commit();

        ImageResultFragment imageResultFragment = new ImageResultFragment();
        FragmentTransaction transaction1 = getSupportFragmentManager().beginTransaction();
        transaction1.replace(R.id.list_fragment, imageResultFragment);
        transaction1.commit();

    }

    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
