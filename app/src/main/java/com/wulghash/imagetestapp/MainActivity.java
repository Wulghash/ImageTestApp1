package com.wulghash.imagetestapp;

import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.wulghash.imagetestapp.ImageWork.ImageFragment;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment;


public class MainActivity extends AppCompatActivity implements ImageResultFragment.OnListFragmentInteractionListener {

    private ImageFragment imageFragment;
    private ImageResultFragment imageResultFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (null == savedInstanceState) {
            initFragments();
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

    @Override
    public void onListFragmentInteraction(ResultImage item) {
        if (imageResultFragment != null)
            imageResultFragment.onShowListDialog(item);
    }



}
