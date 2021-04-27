package com.usccsci571dhruv.uscfilms;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DetailsReviewAdapter extends RecyclerView.Adapter<DetailsReviewAdapter.ViewHolder> {

    private ArrayList<DetailsClasses.Review> mData;
    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public View view;
        public View card;
        public TextView author;
        public TextView stars;
        public TextView content;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            view = itemView;
            card = view.findViewById(R.id.details_review_card);
            author = view.findViewById(R.id.details_review_author);
            stars = view.findViewById(R.id.details_review_stars);
            content = view.findViewById(R.id.details_review_content);
        }
    }

    public DetailsReviewAdapter(Activity activity, ArrayList<DetailsClasses.Review> reviews) {
        this.activity = activity;
        this.mData = reviews;
    }

    @NonNull
    @Override
    public DetailsReviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.details_review_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailsReviewAdapter.ViewHolder holder, int position) {
        DetailsClasses.Review review = mData.get(position);

        String outputDate = "";
        Date date;
        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat outputFormat = new SimpleDateFormat("E, MMM dd yyyy");
        try {
            date = inputFormat.parse(review.created_at);
            outputDate = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String textAuthor = "by " + review.author + " on " + outputDate;
        holder.author.setText(textAuthor);

        String textStar = review.rating + "/5";
        holder.stars.setText(textStar);
        holder.content.setText(review.content);

        holder.view.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ReviewActivity.class);
            intent.putExtra("author", textAuthor);
            intent.putExtra("stars", textStar);
            intent.putExtra("content", review.content);
            activity.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }
}
