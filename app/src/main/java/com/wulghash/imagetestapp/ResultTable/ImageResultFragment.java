package com.wulghash.imagetestapp.ResultTable;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wulghash.imagetestapp.ImageWork.ImageFragment;
import com.wulghash.imagetestapp.Model.ResultImageDatabaseHelper;
import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.Model.ResultImage;

import java.util.ArrayList;


public class ImageResultFragment extends Fragment implements ResultFragmentContract.View {

    private OnListFragmentInteractionListener mListener;
    private ResultImagesRecyclerViewAdapter resultImagesRecyclerViewAdapter;
    private RecyclerView recyclerView;
    private ArrayList<ResultImage> items = new ArrayList<>();
    private ResultFragmentContract.UserActionListener resultFragmentPresenter;
    private ResultImageDatabaseHelper resultImageDatabaseHelper;

    public ImageResultFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        resultFragmentPresenter = new ResultFragmentPresenter(this);
        resultImageDatabaseHelper = new ResultImageDatabaseHelper(getActivity());
        resultFragmentPresenter.onLoadSavedImages(resultImageDatabaseHelper);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            resultImagesRecyclerViewAdapter = new ResultImagesRecyclerViewAdapter(items, mListener, this);
            recyclerView.setAdapter(resultImagesRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void addSavedImagesFromBd(ResultImage resultImage) {
        resultImage.setDownloaded(true);
        addImageToResult(resultImage);
    }

    @Override
    public void addImageToResult(ResultImage resultImage) {
        items.add(resultImage);
        resultImagesRecyclerViewAdapter.notifyItemInserted(items.size()-1);
        recyclerView.smoothScrollToPosition(items.size() - 1);

    }

    public void addImage(ResultImage resultImage) {
        resultFragmentPresenter.onSaveNewImage(resultImage, resultImageDatabaseHelper);
    }

    public void onDeleteFromBd(ResultImage resultImage) {
        resultFragmentPresenter.onDeleteImage(resultImage, resultImageDatabaseHelper);
    }

    @Override
    public void onShowListDialog(ResultImage resultImage) {
        getOptionsDialog(resultImage).show();
    }


    private void onSelectOrDelete(int which, ResultImage resultImage) {
        if (which == 0) {
            resultImagesRecyclerViewAdapter.deleteItem(resultImage);
            onDeleteFromBd(resultImage);
        } else if (which == 1) {
            ImageFragment imageFragment =  (ImageFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.image_fragment);
            imageFragment.setSelectedImage(resultImage);
        }
    }


    public AlertDialog getOptionsDialog(final ResultImage resultImage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.dlg_list_options);
        builder.setItems(R.array.delete_or_select_options, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                onSelectOrDelete(which, resultImage);
            }
        });
        builder.setCancelable(true);

        return builder.create();
    }

    public interface OnListFragmentInteractionListener {
        void onListFragmentInteraction(ResultImage item);
    }

}
