package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ViewHolder> {

    private ArrayList<SearchResultEntry> mData;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final View itemView;
        public final ImageView img;
        public final TextView mediaTypeAndYear;
        public final TextView title;
        public final TextView stars;
        public final CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            this.img = itemView.findViewById(R.id.sCardImg);
            this.mediaTypeAndYear = itemView.findViewById(R.id.sCardMediaType);
            this.title = itemView.findViewById(R.id.sCardTitle);
            this.stars = itemView.findViewById(R.id.sCardStars);
            this.cardView = itemView.findViewById(R.id.sCardView);
        }
    }

    public SearchResultAdapter(Activity activity, ArrayList<SearchResultEntry> dataSet) {
        this.activity = activity;
        this.mData = dataSet;
    }

    @NonNull
    @Override
    public SearchResultAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_result_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchResultAdapter.ViewHolder holder, int position) {
        SearchResultEntry ent = mData.get(position);

        Glide.with(holder.itemView)
                .load(ent.img)
                .fitCenter()
                .into(holder.img);

        String media_type_and_year = "";
        if(ent.media_type.equalsIgnoreCase("movie")) media_type_and_year = "movie";
        if(ent.media_type.equalsIgnoreCase("tv")) media_type_and_year = "tv show";
        media_type_and_year += " (" + ent.year + ")";
        holder.mediaTypeAndYear.setText(media_type_and_year);

        holder.title.setText(ent.name);

        holder.stars.setText(ent.stars);

        holder.cardView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, DetailsActivity.class);
            intent.putExtra("media_type", ent.media_type);
            intent.putExtra("media_id", ent.id);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
