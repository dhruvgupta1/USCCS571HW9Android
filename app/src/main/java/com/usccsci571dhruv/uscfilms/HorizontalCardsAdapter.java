package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class HorizontalCardsAdapter extends RecyclerView.Adapter<HorizontalCardsAdapter.ViewHolder> {

    private Activity activity;
    private ArrayList<ListMediaEntry> mData;
    private boolean isForRecommended;

    public static class ViewHolder extends RecyclerView.ViewHolder {


        public final View itemView;
        public final ImageView img;
        public final ImageView gradient;
        public final ImageView options;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            this.itemView = itemView;
            this.img = itemView.findViewById(R.id.home_card_img);
            this.gradient = itemView.findViewById(R.id.home_card_gradient);
            this.options = itemView.findViewById(R.id.home_card_options);
        }
    }

    public HorizontalCardsAdapter(Activity activity, ArrayList<ListMediaEntry> dataSet) {
        this.activity = activity;
        this.mData = dataSet;
        isForRecommended = false;
    }

    public void setForRecommended(boolean forRecommended) {
        isForRecommended = forRecommended;
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
        if(isForRecommended) {
            holder.gradient.setVisibility(View.GONE);
            holder.options.setVisibility(View.GONE);
        }
        ListMediaEntry ent = mData.get(position);

        Glide.with(holder.itemView)
                .load(ent.poster_path)
                .fitCenter()
                .into(holder.img);

        holder.img.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("media_type", ent.media_type);
            intent.putExtra("media_id", ent.id);
            activity.startActivity(intent);
        });

        holder.options.setOnClickListener(v -> {
            PopupMenu menu = new PopupMenu(holder.itemView.getContext(),holder.options);
            menu.getMenu().add("Open in TMDB");
            menu.getMenu().add("Share on Facebook");
            menu.getMenu().add("Share on Twitter");

            watchlistHandler watchlist = new watchlistHandler(activity);
            watchlistItem witem = new watchlistItem(ent.media_type,
                    ent.id,
                    ent.poster_path,
                    ent.name);
            if(!watchlist.isMediaInWatchlist(witem)) {
                menu.getMenu().add("Add to Watchlist");
            }
            else {
                menu.getMenu().add("Remove from Watchlist");
            }

            menu.setOnMenuItemClickListener(item -> {
                if(item.getTitle().equals("Open in TMDB")) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("https://www.themoviedb.org/" + ent.media_type + "/" + ent.id));
                    activity.startActivity(browserIntent);
                }
                if(item.getTitle().equals("Add to Watchlist")) {
                    watchlist.add(witem);
                    Toast.makeText(activity, witem.name + " was added to Watchlist", Toast.LENGTH_SHORT).show();
                }
                if(item.getTitle().equals("Remove from Watchlist")) {
                    watchlist.remove(witem);
                    Toast.makeText(activity, witem.name + " was removed from Watchlist", Toast.LENGTH_SHORT).show();
                }
                if(item.getTitle().equals("Share on Facebook")) {
                    String link = "https://www.facebook.com/sharer/sharer.php?u=https://www.themoviedb.org/" + ent.media_type +"/" + ent.id;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link));
                    activity.startActivity(browserIntent);
                }
                if(item.getTitle().equals("Share on Twitter")) {
                    String link = "https://twitter.com/intent/tweet?text=Check this out! https://www.themoviedb.org/" + ent.media_type +"/" + ent.id;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(link));
                    activity.startActivity(browserIntent);
                }
                return true;
            });
            menu.show();
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
