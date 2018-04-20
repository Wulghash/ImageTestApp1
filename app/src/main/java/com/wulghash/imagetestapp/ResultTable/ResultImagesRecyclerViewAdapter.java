package com.wulghash.imagetestapp.ResultTable;

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
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wulghash.imagetestapp.R;
import com.wulghash.imagetestapp.ResultImage;
import com.wulghash.imagetestapp.ResultTable.ImageResultFragment.OnListFragmentInteractionListener;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class ResultImagesRecyclerViewAdapter extends RecyclerView.Adapter<ResultImagesRecyclerViewAdapter.ViewHolder> {

    private final List<ResultImage> mValues;
    private final OnListFragmentInteractionListener mListener;


    public ResultImagesRecyclerViewAdapter(List<ResultImage> items, OnListFragmentInteractionListener listener) {
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
        setupProgressBar(holder);
        holder.imageView.setImageBitmap(mValues.get(position).getBitmap());
        setupImage(holder.imageView, mValues.get(position).getMode());
     //   holder.progressBarView.setText(mValues.get(position).content);
        if (position % 2 == 0) holder.mView.setBackgroundColor(Color.GRAY);
        else holder.mView.setBackgroundColor(Color.WHITE);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(holder.mItem);
                    Bitmap bitmap = ((BitmapDrawable)holder.imageView.getDrawable()).getBitmap();
                    holder.mItem.setBitmap(bitmap);
                }
            }
        });
    }

    private void setupProgressBar(final ViewHolder holder) {
        Random r = new Random();
        int i1 = r.nextInt(30 - 5) + 5;
        long loong = i1;
        holder.progressBarView.setVisibility(View.VISIBLE);
        holder.imageView.setVisibility(View.GONE);
        Observable.intervalRange(0L, 100L, 0, loong*10, TimeUnit.MILLISECONDS,
                AndroidSchedulers.mainThread())
                .subscribe(new DisposableObserver<Long>() {
                    @Override
                    public void onComplete() {
                        holder.progressBarView.setVisibility(View.GONE);
                        holder.imageView.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onNext(Long o) {
                        long l = o;
                        int i = (int) l;
                        holder.progressBarView.setProgress(i);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }
                });
    }

    private void setupImage(ImageView imageView, int mode) {
        if (mode == 1) {
            Bitmap myImg = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

            Matrix matrix = new Matrix();
            matrix.postRotate(90);

            Bitmap rotated = Bitmap.createBitmap(myImg, 0, 0, myImg.getWidth(), myImg.getHeight(),
                    matrix, true);

            imageView.setImageBitmap(rotated);
        }

        if (mode == 2) {
            Bitmap myImg = ((BitmapDrawable)imageView.getDrawable()).getBitmap();
            ColorMatrix bwMatrix =new ColorMatrix();
            bwMatrix.setSaturation(0);
            final ColorMatrixColorFilter colorFilter= new ColorMatrixColorFilter(bwMatrix);
            Bitmap rBitmap = myImg.copy(Bitmap.Config.ARGB_8888, true);
            Paint paint = new Paint();
            paint.setColorFilter(colorFilter);
            Canvas myCanvas = new Canvas(rBitmap);
            myCanvas.drawBitmap(rBitmap, 0, 0, paint);

            imageView.setImageBitmap(rBitmap);
        }

        if (mode == 3) {
            Bitmap bitmap = ((BitmapDrawable)imageView.getDrawable()).getBitmap();

            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            Matrix matrix = new Matrix();
            matrix.preScale(-1, 1);

            Bitmap flipImage = Bitmap.createBitmap(bitmap, 0,0 , width, height, matrix, true);

            imageView.setImageBitmap(flipImage);
        }
    }




    public void addItem(ResultImage resultImage) {
        mValues.add(resultImage);
        notifyItemInserted(mValues.size()-1);
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
