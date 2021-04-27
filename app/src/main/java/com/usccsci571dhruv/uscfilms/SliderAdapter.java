package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.smarteist.autoimageslider.SliderViewAdapter;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.glide.transformations.BlurTransformation;

public class SliderAdapter extends SliderViewAdapter<SliderAdapter.SliderAdapterViewHolder> {

    private final List<ListMediaEntry> mSliderItems;
    private final Activity activity;

    public SliderAdapter(Activity activity, ArrayList<ListMediaEntry> sliderDataArrayList) {
        this.mSliderItems = sliderDataArrayList;
        this.activity = activity;
    }
    @Override
    public SliderAdapter.SliderAdapterViewHolder onCreateViewHolder(ViewGroup parent) {
        View inflate = LayoutInflater.from(parent.getContext()).inflate(R.layout.slider_layout, null);
        return new SliderAdapterViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(SliderAdapterViewHolder viewHolder, int position) {
        final ListMediaEntry sliderItem = mSliderItems.get(position);

        Glide.with(viewHolder.itemView)
                .load(sliderItem.poster_path)
                .apply(RequestOptions.bitmapTransform(new BlurTransformation()))
                .into(viewHolder.blurredBackground);
        Glide.with(viewHolder.itemView)
                .load(sliderItem.poster_path)
                .fitCenter()
                .into(viewHolder.mainPoster);

        View.OnClickListener listener = v -> {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("media_type", sliderItem.media_type);
            intent.putExtra("media_id", sliderItem.id);
            activity.startActivity(intent);
        };
        viewHolder.blurredBackground.setOnClickListener(listener);
        viewHolder.mainPoster.setOnClickListener(listener);
    }

    @Override
    public int getCount() {
        return mSliderItems.size();
    }

    static class SliderAdapterViewHolder extends SliderViewAdapter.ViewHolder {
        // Adapter class for initializing
        // the views of our slider view.
        View itemView;
        ImageView blurredBackground;
        ImageView mainPoster;

        public SliderAdapterViewHolder(View itemView) {
            super(itemView);
            blurredBackground = itemView.findViewById(R.id.sliderBlurredImg);
            mainPoster = itemView.findViewById(R.id.sliderPosterImg);
            this.itemView = itemView;
        }
    }
}
