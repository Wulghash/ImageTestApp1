package com.wulghash.imagetestapp.ResultTable;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.Model.ResultImage;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;


public class ResultImagesRecyclerViewAdapter extends RecyclerView.Adapter<ResultImagesRecyclerViewAdapter.ViewHolder> {

    private final List<ResultImage> mValues;
    private final OnListFragmentInteractionListener mListener;
    private ImageResultFragment fragment;



    public ResultImagesRecyclerViewAdapter(List<ResultImage> items, OnListFragmentInteractionListener listener, ImageResultFragment fragment) {
        mValues = items;
        mListener = listener;
        this.fragment = fragment;
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
        final ResultImage resultImage = holder.mItem;
        if (!resultImage.isDownloaded()) setupProgressBar(holder);
         holder.imageView.setImageURI(resultImage.getUri());

        if (position % 2 == 0) holder.mView.setBackgroundColor(Color.GRAY);
        else holder.mView.setBackgroundColor(Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultImage.isDownloaded()) {
                    Bitmap bitmap = ((BitmapDrawable) holder.imageView.getDrawable()).getBitmap();
                    resultImage.setBitmap(bitmap);
                    fragment.onShowListDialog(resultImage);
                }
                }

        });
    }

    private void setupProgressBar(final ViewHolder holder) {
        Random r = new Random();
        final int processSpeed = (r.nextInt(30 - 5) + 5) * 10;

        holder.progressBarView.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.GONE);

        Observable.range(holder.mItem.getProgress(), 100).subscribeOn(Schedulers.computation())
                .concatMap(new Func1<Integer, Observable<?>>() {
                               @Override
                               public Observable<?> call(final Integer v) {
                                   return Observable.timer(holder.mItem.getProcessSpeed() == 0 ? processSpeed : holder.mItem.getProcessSpeed()
                                           ,TimeUnit.MILLISECONDS
                                           ,AndroidSchedulers.mainThread())
                                           .map(new Func1<Long, Object>() {
                                               @Override
                                               public Object call(Long w) {
                                                   return v;
                                               }
                                           });
                               }
                           }).subscribe(new Action1<Object>() {
                    @Override
                    public void call(Object v) {
                        if (holder.mItem.getProcessSpeed() == 0) {
                            holder.mItem.setProcessSpeed(processSpeed);
                        }
                        Integer integer = (int) v;
                        holder.mItem.setProgress(integer);
                        holder.progressBarView.setProgress(integer);
                        if (integer == 99) {
                            holder.progressBarView.setVisibility(View.GONE);
                            holder.imageView.setVisibility(View.VISIBLE);
                            holder.mItem.setDownloaded(true);
                        }
                        }
                    });

    }




    public void deleteItem(ResultImage resultImage) {
        int index = mValues.indexOf(resultImage);
        mValues.remove(index);
        notifyItemRemoved(index);
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final ImageView imageView;
        public final ProgressBar progressBarView;
        public ResultImage mItem;

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
