package com.usccsci571dhruv.uscfilms;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HorizontalCardsAdapter extends RecyclerView.Adapter<HorizontalCardsAdapter.ViewHolder> {

    private ArrayList<ListMediaEntry> mData;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final View itemView;
        public final ImageView img;
        public final ImageView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.img = itemView.findViewById(R.id.home_card_img);
            this.options = itemView.findViewById(R.id.home_card_options);
        }
    }

    public HorizontalCardsAdapter(ArrayList<ListMediaEntry> dataSet) {
        mData = dataSet;
    }

    @NonNull
    @Override
    public HorizontalCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.home_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HorizontalCardsAdapter.ViewHolder holder, int position) {
        ListMediaEntry ent = mData.get(position);

        Glide.with(holder.itemView)
                .load(ent.poster_path)
                .fitCenter()
                .into(holder.img);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
