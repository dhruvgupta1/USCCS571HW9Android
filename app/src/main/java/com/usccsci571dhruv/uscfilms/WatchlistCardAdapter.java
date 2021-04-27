package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class WatchlistCardAdapter extends RecyclerView.Adapter<WatchlistCardAdapter.ViewHolder>
        implements ItemMoveCallback.ItemTouchHelperContract{

    private ArrayList<watchlistItem> mData;
    private Activity activity;

    @Override
    public void onItemMoved(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mData, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mData, i, i - 1);
            }
        }
        watchlistHandler handler = new watchlistHandler(activity);
        handler.set(mData);
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemSelected(ViewHolder viewHolder) {
        viewHolder.selectedOverlay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClear(ViewHolder viewHolder) {
        viewHolder.selectedOverlay.setVisibility(View.INVISIBLE);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public View itemView;
        public ImageView img;
        public ImageView removeBtn;
        public TextView media;
        public ImageView selectedOverlay;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.img = itemView.findViewById(R.id.wCardImg);
            this.removeBtn = itemView.findViewById(R.id.wCardRemove);
            this.media = itemView.findViewById(R.id.wCardMedia);
            this.selectedOverlay = itemView.findViewById(R.id.wCardSelectedOverlay);
        }
    }

    public WatchlistCardAdapter(ArrayList<watchlistItem> dataSet, Activity act) {
        mData = dataSet;
        activity = act;
    }

    @NonNull
    @Override
    public WatchlistCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.watchlist_card_layout, parent, false);

        return new WatchlistCardAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WatchlistCardAdapter.ViewHolder holder, int position) {
        watchlistItem item = mData.get(position);

        Glide.with(holder.itemView)
                .load(item.img)
                .fitCenter()
                .into(holder.img);

        String media = "TV";
        if(item.media_type.equalsIgnoreCase("movie")) media = "Movie";
        holder.media.setText(media);

        holder.removeBtn.setOnClickListener(v -> {
            watchlistHandler handler = new watchlistHandler(activity);
            handler.remove(item);
            mData.remove(position);
            notifyDataSetChanged();
            TextView no_results = activity.findViewById(R.id.watchlist_nothing_saved);
            if(mData.size() == 0) no_results.setVisibility(View.VISIBLE);
            else no_results.setVisibility(View.GONE);

            Toast.makeText(activity, item.name + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
        });

        holder.img.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("media_type", item.media_type);
            intent.putExtra("media_id", item.id);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
