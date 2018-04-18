package com.wulghash.imagetestapp.ResultTable;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment.OnListFragmentInteractionListener;

import java.util.List;

public class ResultImagesRecyclerViewAdapter extends RecyclerView.Adapter<ResultImagesRecyclerViewAdapter.ViewHolder> {

    private final List<Bitmap> mValues;
    private final OnListFragmentInteractionListener mListener;

    public ResultImagesRecyclerViewAdapter(List<Bitmap> items, OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_result_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imageView.setImageBitmap(mValues.get(position));
     //   holder.progressBarView.setText(mValues.get(position).content);
        if (position % 2 == 0) holder.mView.setBackgroundColor(Color.GRAY);
        else holder.mView.setBackgroundColor(Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }


    public void addItem(Bitmap bitmap) {
        mValues.add(0, bitmap);
       // notifyDataSetChanged();
        notifyItemInserted(0);

    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final ProgressBar progressBarView;
        public Bitmap mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            imageView =  view.findViewById(R.id.result_image);
            progressBarView =  view.findViewById(R.id.progress_image_id);
        }

        @Override
        public String toString() {
            return super.toString();
        }
    }
}
