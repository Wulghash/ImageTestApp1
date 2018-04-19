package com.wulghash.imagetestapp.ResultTable;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wulghash.imagetestapp.ImageWork.ImageFragment;
import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.ResultImage;

import java.util.ArrayList;


public class ImageResultFragment extends Fragment implements ResultFragmentContract.View {

    private OnListFragmentInteractionListener mListener;
    private ResultImagesRecyclerViewAdapter resultImagesRecyclerViewAdapter;
    private RecyclerView recyclerView;

    public ImageResultFragment() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static ImageResultFragment newInstance(int columnCount) {
        ImageResultFragment fragment = new ImageResultFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image_list, container, false);
        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            resultImagesRecyclerViewAdapter = new ResultImagesRecyclerViewAdapter(new ArrayList<ResultImage>(), mListener);
            recyclerView.setAdapter(resultImagesRecyclerViewAdapter);
        }
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void addImage(ResultImage resultImage) {
        resultImagesRecyclerViewAdapter.addItem(resultImage);
        recyclerView.smoothScrollToPosition(resultImagesRecyclerViewAdapter.getItemCount()-1);

    }

    @Override
    public void onShowListDialog(ResultImage resultImage) {
        getOptionsDialog(resultImage).show();
    }


    private void onSelectOrDelete(int which, ResultImage resultImage) {
        if (which == 0) {
            resultImagesRecyclerViewAdapter.deleteItem(resultImage);
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
